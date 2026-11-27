@echo off
chcp 65001 >nul
cd /d "%~dp0"

if not exist out\console mkdir out\console

:: Bien dich ma nguon
javac -encoding UTF-8 -d out\console -sourcepath src src\Main.java
if errorlevel 1 (
    echo [LOI] Bien dich that bai. Vui long kiem tra lai JDK!
    pause
    exit /b 1
)

:: Neu duoc goi tu che do cua so rieng (--terminal), chay chuong trinh va pause khi thoat
if "%1"=="--terminal" (
    cls
    java -Dfile.encoding=UTF-8 -cp out\console Main
    echo.
    echo Chuong trinh da ket thuc. Nhan phim bat ky de dong cua so nay...
    pause >nul
    exit /b 0
)

:: Kiem tra xem co dang chay trong moi truong khong ho tro ban phim (Output pane cua IDE)
for /f "tokens=*" %%i in ('powershell -NoProfile -Command "[Console]::IsInputRedirected"') do set REDIRECTED=%%i

if "%REDIRECTED%"=="True" (
    echo [THONG BAO] Phat hien dang chay tren Output khong tuong tac!
    echo Dang khoi dong Console tren cua so Terminal rieng de ban thao tac menu...
    start "OOP_26 - Cua Hang Dung Cu The Thao" cmd /c "%~f0 --terminal"
    exit /b 0
)

:: Neu da o trong Terminal tuong tac, chay truc tiep
java -Dfile.encoding=UTF-8 -cp out\console Main %*
