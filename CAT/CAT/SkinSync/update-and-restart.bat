@echo off
echo ========================================
echo Updating SkinSync Application
echo ========================================

set TOMCAT_HOME=apache-tomcat-9.0.95
set SERVLET_JAR=%TOMCAT_HOME%\lib\servlet-api.jar
set GSON_JAR=web\WEB-INF\lib\gson-2.10.1.jar
set SRC_DIR=src
set CLASSES_DIR=web\WEB-INF\classes

echo.
echo Step 1: Recompiling Java files...
javac -cp "%SERVLET_JAR%;%GSON_JAR%" -d "%CLASSES_DIR%" "%SRC_DIR%\com\skinsync\model\*.java" "%SRC_DIR%\com\skinsync\util\*.java" "%SRC_DIR%\com\skinsync\controller\*.java"

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
) else (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Redeploying to Tomcat...
if exist "%TOMCAT_HOME%\webapps\SkinSync" (
    rmdir /s /q "%TOMCAT_HOME%\webapps\SkinSync"
)

xcopy /E /I /Y "web" "%TOMCAT_HOME%\webapps\SkinSync" >nul
echo Application redeployed!

echo.
echo ========================================
echo Update Complete!
echo ========================================
echo.
echo Please restart the server by:
echo 1. Press Ctrl+C in the server terminal
echo 2. Run start-server.bat again
echo.
pause
