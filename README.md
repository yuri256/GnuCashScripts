# GnuCash Scripts ![Gradle build](https://github.com/yuri256/gnuCashScripts/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=main)
Scripts to make GnuCash import easier:
 - Convert Netherlands' ING CSV export file (new format with ";") to MT940 format consumable by GnuCash
 - Convert Netherlands' ABN AMRO MT940 export file to MT940 format consumable by GnuCash
 - Additional convenience commands to move files around. This requires configuration file to be set up
 - Some filtering / data cleanup during conversion

## Installation

#### Quick run
1. Make sure you have Java version 11+ installed. To check, open Command Prompt (cmd.exe) and run:
    ```cmd
    C:\Users\myuser>java -version
    ...
    OpenJDK Runtime Environment (build 14.0.2+12-46)
    ...
    ```
    In this case java runtime environment of version 14 is installed.
2. Download the *gnuCashScripts.jar* file from the Releases section
3. Run the scripts: 
   ```java -jar gnuCashScripts.jar ingfile -f <C:\path\to\file>```  
   ```java -jar gnuCashScripts.jar abnfile -f <C:\path\to\file>```  
   Those commands will produce a converted file next to the input file, with extension *.mt940*.  
   They don't require configuration file and will use defaults for statement conversion.

#### Complete installation
1. Make sure you have Java version 11+ installed. To check, open *Command Prompt (cmd.exe)* and run:
    ```cmd
    C:\Users\myuser>java -version
    ...
    OpenJDK Runtime Environment (build 14.0.2+12-46)
    ...
    ```
    In this case java runtime environment of version 14 is installed.
1. Make 'bin' directory in your user's home directory:
    ```
    C:\Users\myuser>mkdir bin
    ```
1. Download the *gnuCashScripts.jar* file from the [Releases](https://github.com/yuri256/GnuCashScripts/releases) section to the *bin* directory
1. Copy the [*gnuCashScripts.bat*](https://github.com/yuri256/GnuCashScripts/blob/main/deployment/gnuCashScripts.bat) file to the gnuCashScripts.bat in the *bin* directory
1. Now you should be able to run scripts using command:
    ```
    C:\Users\myuser>bin\gnuCashScripts.bat
    ```
1. Optionally: Add bin directory to the %Path% environment variable. This way you can run the scripts from any directory
  - This PC | Properties | Advanced system settings | *Environment Variables* button
  - Under *User variables for myuser* select
  - Path | Edit | New
  - Add `C:\Users\myuser\bin`
  - OK | OK | OK
  - Open *new* Command Prompt. Now you can just run: 
    ```
    C:\Users\myuser>gnuCashScripts.bat
    ```
1. Make directory for configuration file
    ```
    C:\Users\myuser>mkdir .config\gnuCashScripts  
    ```
1. Copy the [*gnuCashScripts.bat*](https://github.com/yuri256/GnuCashScripts/blob/main/conf/gnuCashScripts.conf.example) file to the *.config\gnuCashScripts\gnuCashScripts.conf* file
1. Edit the file and set values for mandatory options
1. Now you can run:  
```gnuCashScripts.bat move``` to move ING abd ABN AMRO files from the *Downloads* to corresponding ING / ABN AMRO *in* directories    
```gnuCashScripts.bat ing```  to convert ING file from *in* directory. Store results in *out* directory and also copy it to the gnuCash *in* directory   
```gnuCashScripts.bat abn```  to convert ABN AMRO file from *in* directory. Store results in *out* directory and also copy it to the gnuCash *in* directory  
```gnuCashScripts.bat done``` to move files from *gnuCash* *in* directory to *done* directory.  


#### Run from the source code
 1. Make sure you have git and **JDK** version 11+ installed.  
    To check, run `` in the command line. You should get output similar to this:
    ```
    C:\Users\myuser>javac -version
    javac 14.0.2
    ```
2. Clone this repo
3. Deploy
```
cd <project dir>
gradlew jar
java -jar build\libs\gnuCashScripts.jar ingfile -f <path/to/file>
java -jar build\libs\gnuCashScripts.jar abnfile -f <path/to/file>

gradlew deploy
copy C:\Users\myuser\.config\gnuCashScripts\gnuCashScripts.conf.example C:\Users\myuser\.config\gnuCashScripts\gnuCashScripts.conf
notepad.exe C:\Users\myuser\.config\gnuCashScripts\gnuCashScripts.conf

C:\Users\myuser>bin\gnuCashScripts.bat
```  
