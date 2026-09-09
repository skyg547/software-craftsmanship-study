package com.ho.study.algorithm.day01;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ✍️ [Day 1 실습] Two Sum 직접 타이핑 훈련 템플릿
 * 
 * [3회 타이핑 훈련 규칙]
 * 1회차: TwoSum.java를 보면서 아래 주석 순서대로 손으로 직접 입력
 * 2회차: TwoSum.java를 닫고 본 파일의 TODO만 보고 스스로 완성
 * 3회차: 20분 휴식 후 백지 상태에서 다시 작성하여 main 실행 검증
 */
public final class TwoSumPractice {

    private TwoSumPractice() {
    }

    /**
     * TODO: 아래 메서드를 HashMap 1-Pass 방식으로 직접 작성해보세요.
     * 
     * 사고 순서:
     * 1. 숫자와 위치(인덱스)를 기억할 메모장(Map) 생성
     * 2. 배열을 0번부터 끝까지 1회 순회
     * 3. 현재 숫자(currentNumber) 확인
     * 4. 필요한 짝(requiredNumber = target - currentNumber) 계산
     * 5. Map에서 필요한 짝이 이미 있는지 조회
     * 6. 있다면 -> [조회된 인덱스, 현재 인덱스] 즉시 반환
     * 7. 없다면 -> 현재 숫자를 Map에 저장 (반드시 조회 후 저장!)
     * 8. 루프 종료 후에도 못 찾으면 예외 던지기
     */
    public static int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> indexByNumber = new HashMap<>();

        for (int currentIndex = 0; currentIndex < numbers.length; currentIndex++) {
            int currentNumber = numbers[currentIndex];
            int requiredNumber = target - currentNumber;

            Integer requiredIndex = indexByNumber.get(requiredNumber);
            if (requiredIndex != null) {
                return new int[]{requiredIndex, currentIndex};
            }

            indexByNumber.put(currentNumber, currentIndex);
        }

        throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
    }

    public static void main(String[] args) {
        int[] result = twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println("직접 타이핑한 Two Sum 실행 결과: " + Arrays.toString(result));
        assert Arrays.equals(result, new int[]{0, 1}) : "타이핑 검증 실패!";
        System.out.println("🎉 축하합니다! 3회 타이핑 훈련 검증을 성공적으로 통과했습니다.");
    }
}
