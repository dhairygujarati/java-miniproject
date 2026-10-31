@echo off
setlocal
if exist out rmdir /s /q out
mkdir out
if exist sources.txt del sources.txt
dir /s /b src\main\java\*.java > sources.txt
javac --release 17 -Xlint:all -d out @sources.txt
if errorlevel 1 exit /b 1
> manifest.mf echo Main-Class: minibank.MiniBank
jar cfm minibank.jar manifest.mf -C out .
if errorlevel 1 exit /b 1
echo Build successful. Run: java -jar minibank.jar
