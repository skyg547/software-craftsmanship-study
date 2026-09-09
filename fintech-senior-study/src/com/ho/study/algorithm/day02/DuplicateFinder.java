package com.ho.study.algorithm.day02;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Day 2 알고리즘 1: 첫 번째 중복 문자 찾기
 * 
 * [자료구조 선택의 본질]
 * "단순 존재 여부(중복 여부)만 판별하면 된다" ➔ HashSet 선택!
 * 
 * [금융/분산 시스템 실무 매핑]
 * • 결제 멱등성(Idempotency) 검증: "이 transactionId를 이미 처리했는가?"
 * • Kafka 중복 컨슘 방지: "이 eventId를 이미 처리했는가?"
 */
public final class DuplicateFinder {

    private DuplicateFinder() {
    }

    /**
     * Set.add()의 boolean 반환값을 활용한 간결하고 빠른 중복 탐색
     * 
     * set.add(item)은:
     * - 새로운 원소이면 true 반환
     * - 이미 존재하는 원소이면 false 반환
     */
    public static Optional<Character> findFirstDuplicate(String value) {
        Objects.requireNonNull(value, "문자열은 null일 수 없습니다.");

        Set<Character> seenCharacters = new HashSet<>();

        for (char currentCharacter : value.toCharArray()) {
            // 이미 등록되어 있어 add 실패(false)하면 최초 중복 문자 발견!
            if (!seenCharacters.add(currentCharacter)) {
                return Optional.of(currentCharacter);
            }
        }

        return Optional.empty();
    }

    public static void main(String[] args) {
        System.out.println("=== [Day 2-1] 첫 번째 중복 문자 찾기 테스트 시작 ===");

        // Test Case 1: "banking" ➔ 'n'이 최초 중복
        String text1 = "banking";
        Optional<Character> result1 = findFirstDuplicate(text1);
        assert result1.isPresent() && result1.get() == 'n' : "Test 1 실패: " + result1;
        System.out.println("Test 1 통과 (입력: \"" + text1 + "\") ➔ 최초 중복: '" + result1.get() + "'");

        // Test Case 2: 중복이 없는 경우
        String text2 = "craftsman";
        Optional<Character> result2 = findFirstDuplicate(text2);
        assert result2.isPresent() && result2.get() == 'a' : "Test 2 실패: " + result2;
        System.out.println("Test 2 통과 (입력: \"" + text2 + "\") ➔ 최초 중복: '" + result2.get() + "'");

        // Test Case 3: 중복이 전혀 없는 경우
        String text3 = "toss";
        Optional<Character> result3 = findFirstDuplicate(text3);
        assert result3.isPresent() && result3.get() == 's' : "Test 3 실패: " + result3;
        System.out.println("Test 3 통과 (입력: \"" + text3 + "\") ➔ 최초 중복: '" + result3.get() + "'");

        System.out.println("=== [Day 2-1] DuplicateFinder 모든 검증 완료 (SUCCESS) ===\n");
    }
}
