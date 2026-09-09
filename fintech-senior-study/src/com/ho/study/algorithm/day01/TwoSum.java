package com.ho.study.algorithm.day01;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Day 1 알고리즘: Two Sum (두 수의 합)
 * 
 * 1. Brute Force: O(N^2) 시간, O(1) 공간
 * 2. 1-Pass HashMap: O(N) 시간, O(N) 공간
 * 3. Modern Clean Java: 불변 Record IndexPair, Map 초기 용량 최적화, 방어적 검증
 */
public final class TwoSum {

    private TwoSum() {
    }

    /**
     * 방법 1: 완전 탐색 (Brute Force) - O(N^2)
     */
    public static int[] findTwoSumBruteForce(int[] numbers, int target) {
        Objects.requireNonNull(numbers, "배열은 null일 수 없습니다.");

        for (int firstIndex = 0; firstIndex < numbers.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < numbers.length; secondIndex++) {
                if (numbers[firstIndex] + numbers[secondIndex] == target) {
                    return new int[]{firstIndex, secondIndex};
                }
            }
        }

        throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
    }

    /**
     * 방법 2: 1-Pass HashMap 표준 풀이 - O(N)
     */
    public static int[] findTwoSumIndices(int[] numbers, int target) {
        Objects.requireNonNull(numbers, "배열은 null일 수 없습니다.");

        Map<Integer, Integer> indexByNumber = new HashMap<>();

        for (int currentIndex = 0; currentIndex < numbers.length; currentIndex++) {
            int currentNumber = numbers[currentIndex];
            int requiredNumber = target - currentNumber;

            Integer requiredIndex = indexByNumber.get(requiredNumber);
            if (requiredIndex != null) {
                // 이전에 발견된 인덱스가 앞서므로 [requiredIndex, currentIndex] 반환
                return new int[]{requiredIndex, currentIndex};
            }

            // 동일 원소의 중복 사용을 방지하기 위해 '조회 후 저장' 순서 유지
            indexByNumber.put(currentNumber, currentIndex);
        }

        throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
    }

    /**
     * 6년 차 시니어 관점: 도메인 의미를 담는 불변 Record
     */
    public record IndexPair(int firstIndex, int secondIndex) {
        public IndexPair {
            if (firstIndex == secondIndex) {
                throw new IllegalArgumentException("두 인덱스는 동일할 수 없습니다: " + firstIndex);
            }
        }

        public int[] toArray() {
            return new int[]{firstIndex, secondIndex};
        }
    }

    /**
     * 방법 3: Modern Clean Java 풀이 (초기 용량 최적화 + Record 반환)
     */
    public static IndexPair findTwoSumClean(int[] numbers, int target) {
        Objects.requireNonNull(numbers, "배열은 null일 수 없습니다.");
        if (numbers.length < 2) {
            throw new IllegalArgumentException("배열의 길이는 최소 2 이상이어야 합니다.");
        }

        // HashMap 리사이징(Rehashing) 오버헤드 방지를 위한 초기 용량 설정
        int initialCapacity = (int) Math.ceil(numbers.length / 0.75f) + 1;
        Map<Integer, Integer> indexByNumber = new HashMap<>(initialCapacity);

        for (int currentIndex = 0; currentIndex < numbers.length; currentIndex++) {
            int currentNumber = numbers[currentIndex];
            int requiredNumber = target - currentNumber;

            Integer requiredIndex = indexByNumber.get(requiredNumber);
            if (requiredIndex != null) {
                return new IndexPair(requiredIndex, currentIndex);
            }

            indexByNumber.put(currentNumber, currentIndex);
        }

        throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
    }

    public static void main(String[] args) {
        System.out.println("=== [Day 1] Two Sum 알고리즘 테스트 시작 ===");

        // Test Case 1: 기본 케이스
        int[] numbers1 = {2, 7, 11, 15};
        int target1 = 9;
        int[] result1 = findTwoSumIndices(numbers1, target1);
        assert Arrays.equals(result1, new int[]{0, 1}) : "Test 1 실패: " + Arrays.toString(result1);
        System.out.println("Test 1 통과 (numbers={2,7,11,15}, target=9) ➔ " + Arrays.toString(result1));

        // Test Case 2: 원소 순서가 뒤섞인 경우
        int[] numbers2 = {3, 2, 4};
        int target2 = 6;
        int[] result2 = findTwoSumIndices(numbers2, target2);
        assert Arrays.equals(result2, new int[]{1, 2}) : "Test 2 실패: " + Arrays.toString(result2);
        System.out.println("Test 2 통과 (numbers={3,2,4}, target=6) ➔ " + Arrays.toString(result2));

        // Test Case 3: 동일한 값이 2번 등장하는 경우 (중복 처리 검증)
        int[] numbers3 = {3, 3};
        int target3 = 6;
        int[] result3 = findTwoSumIndices(numbers3, target3);
        assert Arrays.equals(result3, new int[]{0, 1}) : "Test 3 실패: " + Arrays.toString(result3);
        System.out.println("Test 3 통과 (numbers={3,3}, target=6) ➔ " + Arrays.toString(result3));

        // Test Case 4: Modern Java Clean 풀이 (Record 검증)
        IndexPair cleanPair = findTwoSumClean(numbers1, target1);
        assert cleanPair.firstIndex() == 0 && cleanPair.secondIndex() == 1 : "Clean 풀이 실패";
        System.out.println("Test 4 통과 (Record IndexPair 반환) ➔ " + cleanPair);

        System.out.println("=== [Day 1] Two Sum 모든 검증 완료 (SUCCESS) ===\n");
    }
}
