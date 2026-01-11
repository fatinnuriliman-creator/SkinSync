@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting SkinSync Application
echo ========================================

set TOMCAT_HOME=apache-tomcat-9.0.95
set CATALINA_HOME=%CD%\%TOMCAT_HOME%

echo.
echo Checking if Tomcat is extracted...
if not exist "%TOMCAT_HOME%\bin\catalina.bat" (
    echo Tomcat not found. Please run setup.bat first!
    pause
    exit /b 1
)

echo.
echo Setting up Java environment...

REM Get JAVA_HOME using java properties
for /f "tokens=2 delims==" %%i in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr "java.home"') do (
    set "JAVA_HOME_RAW=%%i"
)

REM Trim leading space
set "JAVA_HOME=!JAVA_HOME_RAW:~1!"
set "JRE_HOME=!JAVA_HOME!"

echo Found Java at: !JAVA_HOME!

echo.
echo Starting Tomcat server...
echo.
echo Application will be available at:
echo http://localhost:8080/SkinSync/
echo.
echo Login credentials:
echo   Admin: admin@skinsync.com / admin123
echo   User:  user@skinsync.com / 123
echo.
echo Press Ctrl+C to stop the server
echo.

cd "%TOMCAT_HOME%\bin"
call catalina.bat run

endlocal
pause
