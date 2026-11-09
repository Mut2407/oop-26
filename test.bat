@echo off
chcp 65001 >nul
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -Xlint:all -d out -sourcepath src src\Main.java tests\KiemThu.java
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -cp out KiemThu
