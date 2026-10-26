@echo off
chcp 65001 >nul
cd /d "%~dp0src"
javac -encoding UTF-8 models\*.java Main.java
if %errorlevel%==0 (
    java Main
) else (
    echo Bien dich that bai!
    pause
)
