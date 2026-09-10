package com.ho.study.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 3 JVM 실습: 단기 수명 객체와 GC 압력 (Young Generation 수거 시나리오)
 * 
 * [동작 원리]
 * 1. createTemporaryObjects() 호출 시마다 1,000개의 byte[](총 약 1MB) 생성
 * 2. 메서드가 종료되면 temporaryObjects 지역 변수(Stack Frame)가 소멸
 * 3. 할당된 List와 byte[]들은 GC Root로부터 Unreachable 상태로 전환되어 Minor GC 수거 대상이 됨
 */
public final class GcPressureExample {

    private GcPressureExample() {
    }

    public static void runSimulation(int rounds) {
        System.out.println("=== [Day 3 JVM] 단기 수명 객체 할당 시뮬레이션 시작 ===");

        long beforeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        for (int round = 0; round < rounds; round++) {
            createTemporaryObjects();
        }

        // System.gc()는 힌트일 뿐 즉시 회수를 보장하지 않으나, 시뮬레이션 후 Young GC 유도
        System.gc();

        long afterMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("임시 객체 100회 생성 및 Unreachable 전환 완료");
        System.out.println("시뮬레이션 전 메모리: " + (beforeMemory / 1024) + " KB");
        System.out.println("시뮬레이션 후 메모리: " + (afterMemory / 1024) + " KB");
        System.out.println("=== [Day 3 JVM] GcPressureExample 시뮬레이션 성공 ===\n");
    }

    private static void createTemporaryObjects() {
        List<byte[]> temporaryObjects = new ArrayList<>();
        for (int index = 0; index < 1_000; index++) {
            temporaryObjects.add(new byte[1024]); // 1KB 배열
        }
        // 메서드 종료 시점: temporaryObjects 참조 변수가 스택에서 pop되면서 객체들은 Unreachable이 됨!
    }

    public static void main(String[] args) {
        runSimulation(100);
    }
}
