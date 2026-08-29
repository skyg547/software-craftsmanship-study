#!/bin/bash
echo "[1/2] Compiling Clean Code Java sources..."
mkdir -p bin
javac -encoding UTF-8 -d bin $(find 01-clean-code/src -name "*.java")

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "[2/2] Running Main Demo & Challenge Harness..."
java -ea -cp bin com.ho.cleancode.Main
