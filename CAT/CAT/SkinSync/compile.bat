@echo off
echo ========================================
echo Compiling SkinSync Java Files
echo ========================================

REM Create classes directory if it doesn't exist
if not exist "web\WEB-INF\classes" mkdir "web\WEB-INF\classes"

REM Set paths
set TOMCAT_HOME=apache-tomcat-9.0.95
set SERVLET_JAR=%TOMCAT_HOME%\lib\servlet-api.jar
set GSON_JAR=web\WEB-INF\lib\gson-2.10.1.jar
set SRC_DIR=src
set CLASSES_DIR=web\WEB-INF\classes

echo.
echo Compiling Java source files...
echo.

REM Compile all Java files
javac -cp "%SERVLET_JAR%;%GSON_JAR%" -d "%CLASSES_DIR%" "%SRC_DIR%\com\skinsync\model\*.java" "%SRC_DIR%\com\skinsync\util\*.java" "%SRC_DIR%\com\skinsync\controller\*.java"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
    echo.
    echo Compiled classes are in: %CLASSES_DIR%
    echo.
) else (
    echo.
    echo ========================================
    echo Compilation failed! Please check errors above.
    echo ========================================
    echo.
)

pause
