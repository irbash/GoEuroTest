GoEuroTest
==========
Readme.txt

The program GoEuroTest can be run with passing 3 parameters as explained below: 
java -jar GoEuroTest.jar "STRING" "URL" "CSV_FILE_NAME"

"STRING": The first parameter is MANDATORY. i.e if the program is run with zero parameters,
then it will not execute and show you the details on console.
****The first parameter should be the search string*****

"URL": The second parameter is OPTIONAL. i.e you can pass it or you can skip it. 
If you pass the second parameter then the default url will be replaced with the parameter
if you skip this parameter then the default url will be used

"CSV_FILE_NAME": The third parameter is OPTIONAL. i.e you can pass it or you can skip it. 
If you pass the third parameter then the file name of csv file will be used as this parameter 
if you skip this parameter then the default file name will be generated ("STRING"_datetime.csv where "STRING" is the first parameter)

NOTE:
Since the second and third parameter are optional, the third parameter should always be the full file name with EXTENSION. 
i.e if the "CSV_FILE_NAME" parameter is not having ".csv" at end then it will be considered as "URL"
