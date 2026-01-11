@echo off
echo ========================================
echo SkinSync Application Setup
echo ========================================

REM Check if Tomcat zip exists
if not exist "apache-tomcat-9.0.95.zip" (
    echo ERROR: apache-tomcat-9.0.95.zip not found!
    echo Please wait for the download to complete.
    pause
    exit /b 1
)

echo.
echo Step 1: Extracting Apache Tomcat...
if exist "apache-tomcat-9.0.95" (
    echo Tomcat already extracted. Skipping...
) else (
    powershell -Command "Expand-Archive -Path 'apache-tomcat-9.0.95.zip' -DestinationPath '.' -Force"
    echo Tomcat extracted successfully!
)

echo.
echo Step 2: Creating classes directory...
if not exist "web\WEB-INF\classes" mkdir "web\WEB-INF\classes"
echo Classes directory ready!

echo.
echo Step 3: Compiling Java source files...
set TOMCAT_HOME=apache-tomcat-9.0.95
set SERVLET_JAR=%TOMCAT_HOME%\lib\servlet-api.jar
set GSON_JAR=web\WEB-INF\lib\gson-2.10.1.jar
set SRC_DIR=src
set CLASSES_DIR=web\WEB-INF\classes

javac -cp "%SERVLET_JAR%;%GSON_JAR%" -d "%CLASSES_DIR%" "%SRC_DIR%\com\skinsync\model\*.java" "%SRC_DIR%\com\skinsync\controller\*.java"

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
) else (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 4: Deploying application to Tomcat...
if exist "%TOMCAT_HOME%\webapps\SkinSync" (
    echo Removing old deployment...
    rmdir /s /q "%TOMCAT_HOME%\webapps\SkinSync"
)

echo Copying application files...
xcopy /E /I /Y "web" "%TOMCAT_HOME%\webapps\SkinSync"
echo Application deployed successfully!

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo To start the server, run: start-server.bat
echo.
echo The application will be available at:
echo http://localhost:8080/SkinSync/
echo.
echo Login credentials:
echo   Admin: admin@skinsync.com / admin123
echo   User:  user@skinsync.com / 123
echo.
pause
