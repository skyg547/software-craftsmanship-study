package com.ho.study;

import com.ho.study.algorithm.day01.TwoSum;
import com.ho.study.algorithm.day01.TwoSumPractice;
import com.ho.study.algorithm.day02.CharacterFrequency;
import com.ho.study.algorithm.day02.DuplicateFinder;
import com.ho.study.algorithm.day03.PalindromeChecker;
import com.ho.study.algorithm.day03.SortedTwoSum;
import com.ho.study.algorithm.day03.SortedTwoSumPractice;
import com.ho.study.jvm.GcPressureExample;
import com.ho.study.jvm.ImmutableMoneyExample;
import com.ho.study.jvm.MemoryLeakExample;
import com.ho.study.jvm.ReferenceExample;

/**
 * 💳 핀테크/빅테크 Senior Backend 마스터 스터디 통합 러너 (Harness)
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("🚀 [Fintech Senior Study] Day 1 ~ Day 3 전체 검증 및 실습 실행");
        System.out.println("================================================================\n");

        // 1. Day 1: Two Sum 알고리즘 (HashMap 1-Pass, Record)
        TwoSum.main(args);
        TwoSumPractice.main(args);

        // 2. Day 2: Set/Map 알고리즘 & Reference/Immutable Money
        DuplicateFinder.main(args);
        CharacterFrequency.main(args);
        ReferenceExample.main(args);
        ImmutableMoneyExample.main(args);

        // 3. Day 3: Two Pointer 알고리즘 & GC/Memory Leak 실습
        SortedTwoSum.main(args);
        SortedTwoSumPractice.main(args);
        PalindromeChecker.main(args);
        GcPressureExample.main(args);
        MemoryLeakExample.main(args);

        System.out.println("================================================================");
        System.out.println("🎉 [Day 1 ~ Day 3] 모든 알고리즘 및 JVM 실습 테스트 100% PASS!");
        System.out.println("================================================================");
    }
}
