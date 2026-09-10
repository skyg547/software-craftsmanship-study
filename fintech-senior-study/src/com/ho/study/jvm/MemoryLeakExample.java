package com.ho.study.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 3 JVM 실습: 정적 참조(Static Reference)로 인한 메모리 누수(Memory Leak) 시뮬레이션
 * 
 * [동작 원리]
 * 1. static 필드는 Metaspace의 Class 객체가 살아있는 한 영구적인 GC Root로 동작합니다.
 * 2. STORAGE에 객체를 지속적으로 add하고 비우지 않으면, 해당 객체들은 영원히 Reachable 상태를 유지하여 GC가 수거할 수 없습니다.
 * 3. 이것이 프로덕션 환경에서 캐시 객체를 잘못 관리할 때 OOM(OutOfMemoryError)이 발생하는 메커니즘입니다.
 */
public final class MemoryLeakExample {

    // GC Root로 살아남는 정적 리스트
    private static final List<byte[]> STATIC_STORAGE = new ArrayList<>();

    private MemoryLeakExample() {
    }

    public static void runSimulation() {
        System.out.println("=== [Day 3 JVM] 정적 참조 메모리 누수 & 정리 시뮬레이션 시작 ===");

        long initialUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // 1. 10MB 분량의 메모리 누적 (Reachable 상태 유지)
        for (int i = 0; i < 10; i++) {
            STATIC_STORAGE.add(new byte[1024 * 1024]); // 1MB 단위 할당
        }

        long filledMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("정적 컬렉션에 10MB 적재 후 사용 메모리: " + (filledMemory / (1024 * 1024)) + " MB");
        assert STATIC_STORAGE.size() == 10 : "정적 스토리지 크기 불일치";

        // 2. 명시적 누수 해결 (참조 해제)
        STATIC_STORAGE.clear();
        System.gc(); // 청소 유도

        System.out.println("STATIC_STORAGE.clear() 호출 ➔ GC Root 연결 끊김 (Unreachable 전환)");
        System.out.println("=== [Day 3 JVM] MemoryLeakExample 시뮬레이션 성공 ===\n");
    }

    public static void main(String[] args) {
        runSimulation();
    }
}
