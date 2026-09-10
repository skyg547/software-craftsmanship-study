package com.ho.study.algorithm.day03;

import java.util.Arrays;

/**
 * ✍️ [Day 3 실습] Sorted Two Sum 직접 타이핑 훈련 템플릿
 * 
 * [3회 타이핑 훈련 규칙]
 * 1회차: SortedTwoSum.java를 보며 Two Pointer 이동 원리를 손으로 타이핑
 * 2회차: 답을 가리고 아래 TODO만 보고 직접 완성
 * 3회차: 20분 뒤 백지 상태에서 다시 작성하여 main 실행 검증
 */
public final class SortedTwoSumPractice {

    private SortedTwoSumPractice() {
    }

    /**
     * TODO: 정렬된 배열에서 두 수의 합을 찾는 Two Pointer 알고리즘을 직접 작성해보세요.
     * 
     * 사고 흐름:
     * 1. leftIndex = 0, rightIndex = length - 1 초기화
     * 2. while (leftIndex < rightIndex) 루프 실행
     * 3. sum = sortedNumbers[leftIndex] + sortedNumbers[rightIndex]
     * 4. sum == target ➔ new int[]{leftIndex, rightIndex} 반환
     * 5. sum < target ➔ 더 큰 숫자가 필요하므로 leftIndex++
     * 6. sum > target ➔ 더 작은 숫자가 필요하므로 rightIndex--
     * 7. 못 찾으면 예외 던지기
     */
    public static int[] findPair(int[] sortedNumbers, int target) {
        int leftIndex = 0;
        int rightIndex = sortedNumbers.length - 1;

        while (leftIndex < rightIndex) {
            int currentSum = sortedNumbers[leftIndex] + sortedNumbers[rightIndex];

            if (currentSum == target) {
                return new int[]{leftIndex, rightIndex};
            }

            if (currentSum < target) {
                leftIndex++;
            } else {
                rightIndex--;
            }
        }

        throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
    }

    public static void main(String[] args) {
        int[] result = findPair(new int[]{1, 2, 4, 6, 10}, 10);
        System.out.println("직접 타이핑한 Sorted Two Sum 실행 결과: " + Arrays.toString(result));
        assert Arrays.equals(result, new int[]{2, 3}) : "타이핑 검증 실패!";
        System.out.println("🎉 [Day 3] Sorted Two Sum 3회 타이핑 검증 성공!");
    }
}
