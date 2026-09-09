@echo off
chcp 65001 > nul
echo ================================================================
echo [1/3] Compiling All Java Study Sources (Clean Code & Fintech)...
echo ================================================================
if not exist "bin" mkdir bin

dir /s /b 01-clean-code\src\*.java fintech-senior-study\src\*.java > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
del sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    exit /b %ERRORLEVEL%
)

echo.
echo ================================================================
echo [2/3] Running 01-clean-code Main Demo & Challenge...
echo ================================================================
java -ea -cp bin com.ho.cleancode.Main

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Clean Code execution failed!
    exit /b %ERRORLEVEL%
)

echo.
echo ================================================================
echo [3/3] Running fintech-senior-study Main Harness...
echo ================================================================
java -ea -cp bin com.ho.study.Main

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Fintech Senior Study execution failed!
    exit /b %ERRORLEVEL%
)
