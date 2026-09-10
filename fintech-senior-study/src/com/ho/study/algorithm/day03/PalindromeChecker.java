package com.ho.study.algorithm.day03;

import java.util.Objects;

/**
 * Day 3 알고리즘 2: 회문 검사 (Palindrome Checker - Two Pointer)
 * 
 * [자료구조 & 알고리즘]
 * • 양쪽 끝에서 시작하여 가운데로 좁혀오며 대칭 검사
 * • 시간복잡도: O(N), 추가 공간복잡도: O(1)
 */
public final class PalindromeChecker {

    private PalindromeChecker() {
    }

    public static boolean isPalindrome(String value) {
        Objects.requireNonNull(value, "문자열은 null일 수 없습니다.");

        int leftIndex = 0;
        int rightIndex = value.length() - 1;

        while (leftIndex < rightIndex) {
            if (value.charAt(leftIndex) != value.charAt(rightIndex)) {
                return false;
            }
            leftIndex++;
            rightIndex--;
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== [Day 3-2] Palindrome 회문 검사기 테스트 시작 ===");

        // Test Case 1: "level" ➔ true
        assert isPalindrome("level") : "level 회문 검사 실패";
        System.out.println("Test 1 통과: \"level\" ➔ 회문 맞음 (true)");

        // Test Case 2: "rotator" ➔ true
        assert isPalindrome("rotator") : "rotator 회문 검사 실패";
        System.out.println("Test 2 통과: \"rotator\" ➔ 회문 맞음 (true)");

        // Test Case 3: "tossbank" ➔ false
        assert !isPalindrome("tossbank") : "tossbank 회문 검사 실패";
        System.out.println("Test 3 통과: \"tossbank\" ➔ 회문 아님 (false)");

        // Test Case 4: 단일 문자 또는 빈 문자열
        assert isPalindrome("a") : "단일 문자 실패";
        assert isPalindrome("") : "빈 문자열 실패";
        System.out.println("Test 4 통과: 단일 문자 / 빈 문자열 ➔ 회문 맞음 (true)");

        System.out.println("=== [Day 3-2] PalindromeChecker 모든 검증 완료 (SUCCESS) ===\n");
    }
}
