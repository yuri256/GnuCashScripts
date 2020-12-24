# GnuCash Scripts ![Gradle build](https://github.com/yuri256/gnuCashScripts/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=main)
Scripts to make GnuCash import easier:
 - Convert Netherlands' ING CSV export file (new format with ";") to MT940 format consumable by GnuCash
 - Convert Netherlands' AbnAmro MT940 export file to MT940 format consumable by GnuCash
 - Additional convenience commands to move files around. This requires configuration file to be set up
 - Some filtering / data cleanup during conversion

## Running the scripts
There are two basic ways to run the scripts:
 1. Using pre-built jar file
 1. Build and run the scripts yourself from the source code

Following sections will describe how to run the scripts using both ways

## Using pre-built jar file
 1. Make sure you have java (JRE) version 11+ installed. To check, run `java -version` in the command line. You should get output similar to this:
    ```
    C:\Users\myuser>java -version
    ...
    OpenJDK Runtime Environment (build 14.0.2+12-46)
    ...
    ```
    In this case java runtime environment of version 14 is installed.
 2. Download the jar file from the Releases section
 3. Run the utility (help):
   ```java -jar gnuCashScripts.jar```
### Convert ING CSV / AbnAmro MT940 file
```java -jar gnuCashScripts.jar ingfile -f <path/to/file>```  
```java -jar gnuCashScripts.jar abnfile -f <path/to/file>```  
This will produce a converted file, with the name of input file and extension '.mt940' next to input file 

## Building the code yourself
 1. Make sure you have git and **JDK** version 11+ installed.  
    To check, run `` in the command line. You should get output similar to this:
    ```
    C:\Users\myuser>javac -version
    javac 14.0.2
    ```
2. Clone the code and then run:
```
cd <project dir>
gradlew jar
java -jar build\libs\gnuCashScripts.jar ingfile -f <path/to/file>
java -jar build\libs\gnuCashScripts.jar abnfile -f <path/to/file>
```  

## More documentation to come :)
