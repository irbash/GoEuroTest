package org.goeuro.sample.BL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;

import org.goeuro.sample.jsonObject.GeoObject;
import org.goeuro.sample.jsonObject.GeoPosition;
import org.goeuro.sample.jsonObject.Results;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetJSONDataAndSaveCSV
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
        } catch (SSLHandshakeException e)
        {
            System.out.println("ERROR: SSL HandShake Exception");
            System.out.println("Please add certificate to jre and run the program again. ");
            System.out.println("STEPS:");
            System.out.println("Download the certificate to local machine");
            System.out.println("Go to  $JAVA_HOME$\\jre\\lib\\security  then run the following command:");
            System.out
                    .println("keytool -import -file [the certificated downloaded from previous step, for example, C:\\api.goeuro.de.crt] -keystore cacerts");
            System.out.println("HINT: 1. Run the cmd prompt as administrator. ");
            System.out.println("HINT: 2. Password for keystore on windows machine is \"changeit\"");

        } catch (IOException e)
        {
            System.out.println("ERROR: The URL is not correct or problem accessing the URL");
            System.out.println("Please check if the below URL is valid");
            System.out.println("URL: " + urlToRead);

        } catch (Exception e)
        {
            System.out.println("Some unexpected error occured. Please try again later.");
            e.printStackTrace();
        }

        return result;
    }

    public Results convertToCSV(String result)
    {
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // parse json string to object
            Results res = gson.fromJson(result, Results.class);
            return res;
        } catch (Exception e)
        {
            System.out.println("Unexpected error occured. ");
            e.printStackTrace();
            return null;
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
            // Check if parent directory exist. If not try to create it.
            File f = new File(filename);
            File parent = new File(f.getParent());
            if (!parent.exists())
            {
                parent.mkdirs();
            }
            // Create CSV file
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
}
