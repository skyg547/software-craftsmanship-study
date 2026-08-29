#!/bin/bash
echo "[1/2] Compiling Clean Code Java sources..."
mkdir -p bin
javac -encoding UTF-8 -d bin 01-clean-code/src/com/ho/cleancode/naming/*.java 01-clean-code/src/com/ho/cleancode/functions/*.java 01-clean-code/src/com/ho/cleancode/optional/*.java 01-clean-code/src/com/ho/cleancode/Main.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "[2/2] Running Main Demo..."
java -cp bin com.ho.cleancode.Main
