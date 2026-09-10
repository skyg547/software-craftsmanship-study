package com.ho.study.algorithm.day03;

import java.util.Arrays;
import java.util.Objects;

/**
 * Day 3 알고리즘 1: 정렬된 배열에서 두 수의 합 (Sorted Two Sum - Two Pointer)
 * 
 * [자료구조 & 알고리즘 선택]
 * • 전제 조건: 오름차순 정렬된 배열
 * • 복잡도: 시간복잡도 O(N), 추가 공간복잡도 O(1)
 * • Two Pointer 원리:
 *   - currentSum == target ➔ [left, right] 반환
 *   - currentSum < target  ➔ 더 큰 합이 필요하므로 left++
 *   - currentSum > target  ➔ 더 작은 합이 필요하므로 right--
 */
public final class SortedTwoSum {

    private SortedTwoSum() {
    }

    public static int[] findPairIndices(int[] sortedNumbers, int target) {
        Objects.requireNonNull(sortedNumbers, "배열은 null일 수 없습니다.");
        if (sortedNumbers.length < 2) {
            throw new IllegalArgumentException("배열의 길이는 최소 2 이상이어야 합니다.");
        }

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
        System.out.println("=== [Day 3-1] Sorted Two Sum (Two Pointer) 테스트 시작 ===");

        // Test Case 1: [1, 2, 4, 6, 10], target = 10 ➔ 4 + 6 = 10 (인덱스 [2, 3])
        int[] numbers1 = {1, 2, 4, 6, 10};
        int target1 = 10;
        int[] result1 = findPairIndices(numbers1, target1);
        assert Arrays.equals(result1, new int[]{2, 3}) : "Test 1 실패: " + Arrays.toString(result1);
        System.out.println("Test 1 통과 (입력: [1,2,4,6,10], target=10) ➔ 결과: " + Arrays.toString(result1));

        // Test Case 2: 양 끝 원소 매칭 [2, 7, 11, 15], target = 17 ➔ 2 + 15 = 17 (인덱스 [0, 3])
        int[] numbers2 = {2, 7, 11, 15};
        int target2 = 17;
        int[] result2 = findPairIndices(numbers2, target2);
        assert Arrays.equals(result2, new int[]{0, 3}) : "Test 2 실패: " + Arrays.toString(result2);
        System.out.println("Test 2 통과 (입력: [2,7,11,15], target=17) ➔ 결과: " + Arrays.toString(result2));

        // Test Case 3: 음수 포함 정렬 배열 [-3, -1, 2, 4, 8], target = 3 ➔ -1 + 4 = 3 (인덱스 [1, 3])
        int[] numbers3 = {-3, -1, 2, 4, 8};
        int target3 = 3;
        int[] result3 = findPairIndices(numbers3, target3);
        assert Arrays.equals(result3, new int[]{1, 3}) : "Test 3 실패: " + Arrays.toString(result3);
        System.out.println("Test 3 통과 (입력: [-3,-1,2,4,8], target=3) ➔ 결과: " + Arrays.toString(result3));

        System.out.println("=== [Day 3-1] Sorted Two Sum 모든 검증 완료 (SUCCESS) ===\n");
    }
}
