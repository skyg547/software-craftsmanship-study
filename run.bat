@echo off
chcp 65001 > nul
echo [1/2] Compiling Clean Code Java sources...
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin 01-clean-code/src/com/ho/cleancode/naming/*.java 01-clean-code/src/com/ho/cleancode/functions/*.java 01-clean-code/src/com/ho/cleancode/optional/*.java 01-clean-code/src/com/ho/cleancode/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    exit /b %ERRORLEVEL%
)

echo [2/2] Running Main Demo...
java -cp bin com.ho.cleancode.Main
