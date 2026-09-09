package com.ho.study;

import com.ho.study.algorithm.day01.TwoSum;
import com.ho.study.algorithm.day01.TwoSumPractice;
import com.ho.study.algorithm.day02.CharacterFrequency;
import com.ho.study.algorithm.day02.DuplicateFinder;
import com.ho.study.jvm.ImmutableMoneyExample;
import com.ho.study.jvm.ReferenceExample;

/**
 * 💳 핀테크/빅테크 Senior Backend 마스터 스터디 통합 러너 (Harness)
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("🚀 [Fintech Senior Study] Day 1 & Day 2 전체 검증 및 실습 실행");
        System.out.println("================================================================\n");

        // 1. Day 1 알고리즘: Two Sum
        TwoSum.main(args);
        TwoSumPractice.main(args);

        // 2. Day 2 알고리즘: DuplicateFinder & CharacterFrequency
        DuplicateFinder.main(args);
        CharacterFrequency.main(args);

        // 3. Day 2 JVM & 금융 실습: Reference & Immutable Money
        ReferenceExample.main(args);
        ImmutableMoneyExample.main(args);

        System.out.println("================================================================");
        System.out.println("🎉 모든 알고리즘 및 JVM 실습 테스트가 100% 성공적으로 통과되었습니다!");
        System.out.println("================================================================");
    }
}
