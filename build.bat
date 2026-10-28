@echo off
if not exist out mkdir out
dir /s /b src\main\java\*.java > sources.txt
javac --release 17 -Xlint:all -d out @sources.txt
if errorlevel 1 exit /b 1
echo Build successful. Run: java -cp out minibank.MiniBank
