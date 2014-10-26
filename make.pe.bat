@echo off
setlocal

REM This file is for building the Poker Enlighter executable.

REM The build log file:
set buildlog=build_.%1_%2.log

REM Some very important notes:
REM - The script expects 2 parameters: the version of Poker Enlighter followed
REM by the build number. For example: "myscript.bat 2.3 538"
REM - The script assumes the availability of the java, javac, jar, xcopy, winrar and md5sum commands.
REM - The script makes very specific assumptions of the structure of the JAR file, in the packaging
REM stage. Please keep this script in sync with this structure.

REM We're going to count execution time. So, remeber start time. Note that we don't look at the date, so this
REM calculation won't work right if the program run spans local midnight.
set t0=%time: =0%

REM First, let's define some variables that contain most used names and paths
REM throughout the build script. Changes of these variables will propagate down the script.

set manifestfile=Manifest.txt
set guijar=Poker_Enlighter.jar
set mainclass=org/javafling/pokerenlighter/main/PokerEnlighter
set simulatorjar=lib/simulator/simulator.jar
set lib1=lib/easynth.lnf/EaSynthLookAndFeel.jar
set lib2=lib/jfreechart/jcommon-1.0.22.jar
set lib3=lib/jfreechart/jfreechart-1.0.18.jar
set lib4=lib/nimrod.lnf/nimrodlf-1.2.jar
set lib5=lib/seaglass.lnf/seaglasslookandfeel-0.2.jar

REM The actual compilation command. It is compiled without any debugging
REM symbols, to add some obfuscation. These symbols will probably be removed by Proguard anyway, but
REM it's not a bad thing to be extra-careful.

javac -g:none -Xlint:unchecked -classpath .;%simulatorjar%;%lib1%;%lib2%;%lib3%;%lib4%;%lib5% %mainclass%.java > %buildlog% 2>&1
timeout /t 1 /nobreak > NUL

REM Next, the script will move inside the "org/" folder and delete all the source code files.
REM This is to ensure that the resulting executable will not contain the source code.
REM The script will move back to the build folder root after it's done deleting.

cd org
del /s *.java >> %buildlog% 2>&1
timeout /t 1 /nobreak > NUL
cd..

REM Next, the Manifest file is built. This is needed for the JAR file.

echo Main-Class: %mainclass% > %manifestfile%
echo Class-Path: %simulatorjar% %lib1% %lib2% %lib3% %lib4% %lib5% >> %manifestfile%

REM Package everything in a JAR file.

jar cfm0 %guijar% %manifestfile% org/* >> %buildlog% 2>&1

REM Next, we package everything in a zip file.
REM - "a" means create archive.
REM - "-afzip" means make it a ZIP archive
REM - "-m0" means no compression

winrar a -afzip -r -m0 "poker.enlighter.%1.%2.zip" LICENSE.txt %guijar% config/* lib/*  >> %buildlog% 2>&1

REM Finally, we need a MD5 hash value for the archive.

md5sum "poker.enlighter.%1.%2.zip" > md5sum.txt

REM And we are done. Enjoy.

REM Capture the end time before doing anything else
set t=%time: =0%

REM make t0 into a scaler in 100ths of a second, being careful not 
REM to let SET/A misinterpret 08 and 09 as octal
set /a h=1%t0:~0,2%-100
set /a m=1%t0:~3,2%-100
set /a s=1%t0:~6,2%-100
set /a c=1%t0:~9,2%-100
set /a starttime = %h% * 360000 + %m% * 6000 + 100 * %s% + %c%

REM make t into a scaler in 100ths of a second
set /a h=1%t:~0,2%-100
set /a m=1%t:~3,2%-100
set /a s=1%t:~6,2%-100
set /a c=1%t:~9,2%-100
set /a endtime = %h% * 360000 + %m% * 6000 + 100 * %s% + %c%

REM runtime in 100ths is now just end - start
set /a runtime = %endtime% - %starttime%
set runtime = %s%.%c%

echo Ran for %runtime%0 ms