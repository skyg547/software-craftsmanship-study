#!/bin/bash
echo "================================================================"
echo "[1/3] Compiling All Java Study Sources (Clean Code & Fintech)..."
echo "================================================================"
mkdir -p bin
javac -encoding UTF-8 -d bin $(find 01-clean-code/src fintech-senior-study/src -name "*.java")

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo ""
echo "================================================================"
echo "[2/3] Running 01-clean-code Main Demo & Challenge..."
echo "================================================================"
java -ea -cp bin com.ho.cleancode.Main

if [ $? -ne 0 ]; then
    echo "[ERROR] Clean Code execution failed!"
    exit 1
fi

echo ""
echo "================================================================"
echo "[3/3] Running fintech-senior-study Main Harness..."
echo "================================================================"
java -ea -cp bin com.ho.study.Main

if [ $? -ne 0 ]; then
    echo "[ERROR] Fintech Senior Study execution failed!"
    exit 1
fi
