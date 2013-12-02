package org.goeuro.sample.main;

import java.util.Date;

import org.goeuro.sample.BL.GetJSONDataAndSaveCSV;
import org.goeuro.sample.jsonObject.Results;

public class GoEuroTest
{

    public static void main(String args[])
    {
        String url;
        String csvFileName = "";
        String STRING = "";
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

            url = "https://api.goeuro.de/api/v1/suggest/position/en/name/";

            if (args != null && args.length > 0)
            {
                STRING = args[0];
                if (args.length == 1)
                {
                    csvFileName = STRING + "_" + new Date().getTime() + ".csv";
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
                        csvFileName = STRING + "_" + new Date().getTime() + ".csv";
                    }
                }
                if (url.endsWith("/"))
                {
                    url += STRING;
                } else
                {
                    url += "/" + STRING;
                }

                GetJSONDataAndSaveCSV getDataAndSave = new GetJSONDataAndSaveCSV();

                String jsonResult = getDataAndSave.getJSON(url);
                if (jsonResult != null && jsonResult.trim().length() > 0)
                {
                    Results results = getDataAndSave.convertToCSV(jsonResult);
                    if (results != null && results.getGeoObjects().size() > 0)
                    {
                        getDataAndSave.createCSV(results, csvFileName);
                        System.out.println("********************************************************");
                        System.out.println("*************************SUCCESS************************");
                        System.out.println("JSON object successfully read and saved as csv file.");
                        System.out.println("********************************************************");
                    } else
                    {
                        System.out.println("********************************************************");
                        System.out.println("*************************SUCCESS************************");
                        System.out.println("The query string (" + STRING
                                + ") returned zero objects, hence CSV file was not created");
                        System.out.println("********************************************************");
                    }

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
