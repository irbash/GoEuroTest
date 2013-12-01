package org.goeuro.sample.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.goeuro.sample.jsonObject.GeoObject;
import org.goeuro.sample.jsonObject.GeoPosition;
import org.goeuro.sample.jsonObject.Results;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoEuroTest
{

    /*
     * getJSON parameter: urlToRead-> url of the service which returns the GEO objects as JSON returns the JSON string
     */
    public String getJSON(String urlToRead)
    {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try
        {
            // Create URL object
            url = new URL(urlToRead);
            // Open connection
            conn = (HttpURLConnection) url.openConnection();
            // Set the request as GET
            conn.setRequestMethod("GET");
            // Send the request and read the response line by line
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null)
            {
                result += line;
            }
            rd.close();
        } catch (IOException e)
        {
            System.out.println("ERROR: The URL is not correct or problem accessing the URL");
            System.out.println("Please check if the below URL is valid");
            System.out.println("URL: " + urlToRead);
            e.printStackTrace();
        } catch (Exception e)
        {
            System.out.println("Some unexpected error occured. Please try again later.");
            e.printStackTrace();
        }

        return result;
    }

    private boolean convertToCSV(String result, String filename)
    {

        // For TESTING
        // Since the url was not returning anything.
        /*
         * if (result.isEmpty()) { result =
         * " { \"results\" : [ { \"_type\" : \"Position\", \"_id\" : 410978, \"name\" : \"Potsdam, USA\", \"type\" : \"location\", \"geo_position\" : { \"latitude\" : 44.66978, \"longitude\" : -74.98131 } },{ \"_type\" : \"Position\", \"_id\" : 410978, \"name\" : \"Potsdam, USA\", \"type\" : \"location\", \"geo_position\" : { \"latitude\" : 44.66978, \"longitude\" : -74.98131 } } ] }"
         * ; }
         */
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // parse json string to object
            Results res = gson.fromJson(result, Results.class);
            return createCSV(res, filename);
        } catch (Exception e)
        {
            System.out.println("Unexpected error occured. ");
            e.printStackTrace();
            return false;
        }

    }

    // Create csv file
    public boolean createCSV(Results results, String filename)
    {
        FileWriter fileWriter;
        if (!filename.toLowerCase().endsWith(".csv"))
        {
            filename += ".csv";
        }
        try
        {
            fileWriter = new FileWriter(filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (GeoObject geoObj : results.getGeoObjects())
            {
                printWriter.print(geoObj.get_type());
                printWriter.print(",");
                printWriter.print(geoObj.get_id());
                printWriter.print(",");
                printWriter.print(geoObj.getName());
                printWriter.print(",");
                printWriter.print(geoObj.getType());
                printWriter.print(",");
                GeoPosition geoPos = geoObj.getGeoPosition();
                printWriter.print(geoPos.getLatitude());
                printWriter.print(",");
                printWriter.println(geoPos.getLongitude());
            }
            // Flush the output to the file
            printWriter.flush();

            // Close the Print Writer
            printWriter.close();

            // Close the File Writer
            fileWriter.close();
        } catch (IOException e)
        {
            System.out.println("Unexpected error while saving csv file. Please check if the path is correct.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String args[])
    {
        String url;
        String csvFileName = "";
        if (args == null || args.length == 0)
        {
            System.out.println("Invalid arguments: \n");
            System.out.println("Run the program with following options: \n");
            System.out.println("java -jar GoEuroTest.jar \"STRING\" \"URL\" \"CSV_FILE_NAME\"\n");
            System.out.println("STRING: Mandatory. Provide the string (name of the place which you want to search) \n");
            System.out
                    .println("URL: OPTIONAL. the url of the service (ex: http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/)\n");
            System.out
                    .println("CSV_FILE_NAME: OPTIONAL. The name of csv file along with extension (.csv) (Default value: STRING_datetime.csv)\n");
        } else
        {

            url = "http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/";

            if (args != null && args.length > 0)
            {
                if (args.length == 1)
                {
                    csvFileName = args[0] + "_" + new Date().getTime() + ".csv";
                } else if (args.length == 3)
                {
                    url = args[1];
                    csvFileName = args[2];
                } else if (args.length == 2)
                {
                    if (args[1].toLowerCase().endsWith("csv"))
                    {
                        csvFileName = args[1];
                    } else
                    {
                        url = args[1];
                        csvFileName = args[0] + "_" + new Date().getTime() + ".csv";
                    }
                }
                if (url.endsWith("/"))
                {
                    url += args[0];
                } else
                {
                    url += "/" + args[0];
                }
                GoEuroTest goEuro = new GoEuroTest();
                String result = goEuro.getJSON(url);
                if (result != null && result.trim().length() > 0 && goEuro.convertToCSV(result, csvFileName))
                {
                    System.out.println("********************************************************");
                    System.out.println("*************************SUCCESS************************");
                    System.out.println("JSON object successfully read and saved as csv file.");
                    System.out.println("********************************************************");
                } else
                {
                    System.out.println("********************************************************");
                    System.out.println("*************************FAILED************************");
                    System.out.println("Some unexpected error occured.");
                    System.out.println("Check the console errors and correct them before trying again");
                    System.out.println("For further assistance contact abc@xyz.com");
                    System.out.println("********************************************************");
                }

            }

        }

    }
}
