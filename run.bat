@echo off
chcp 65001 > nul
echo [1/2] Compiling Clean Code Java sources...
if not exist "bin" mkdir bin

dir /s /b 01-clean-code\src\*.java > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
del sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    exit /b %ERRORLEVEL%
)

echo [2/2] Running Main Demo & Challenge Harness...
java -ea -cp bin com.ho.cleancode.Main
