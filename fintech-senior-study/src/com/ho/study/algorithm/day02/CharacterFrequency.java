package com.ho.study.algorithm.day02;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Day 2 알고리즘 2: 문자 빈도수 카운팅 (Character Frequency)
 * 
 * [자료구조 선택의 본질]
 * "값(문자)과 연결된 다른 정보(카운트/누적 횟수)가 필요하다" ➔ HashMap 선택!
 * 
 * [Modern Java 리팩터링]
 * 기존: if (map.containsKey(k)) map.put(k, map.get(k) + 1); else map.put(k, 1);
 * 개선: map.merge(key, 1, Integer::sum);
 */
public final class CharacterFrequency {

    private CharacterFrequency() {
    }

    /**
     * Map.merge()를 활용한 모던 자바 빈도수 집계
     * - key가 없으면 value(1) 삽입
     * - key가 이미 있으면 remappingFunction(Integer::sum) 실행하여 기존값 + 1
     */
    public static Map<Character, Integer> countCharacters(String value) {
        Objects.requireNonNull(value, "문자열은 null일 수 없습니다.");

        Map<Character, Integer> frequencyByCharacter = new HashMap<>();

        for (char currentCharacter : value.toCharArray()) {
            frequencyByCharacter.merge(currentCharacter, 1, Integer::sum);
        }

        return frequencyByCharacter;
    }

    public static void main(String[] args) {
        System.out.println("=== [Day 2-2] 문자 빈도수 계산 테스트 시작 ===");

        // Test Case 1: "banana" ➔ b:1, a:3, n:2
        String text1 = "banana";
        Map<Character, Integer> freq1 = countCharacters(text1);
        assert freq1.get('b') == 1 : "b 빈도수 불일치";
        assert freq1.get('a') == 3 : "a 빈도수 불일치";
        assert freq1.get('n') == 2 : "n 빈도수 불일치";
        System.out.println("Test 1 통과 (입력: \"" + text1 + "\") ➔ 결과: " + freq1);

        // Test Case 2: "kakaobank"
        String text2 = "kakaobank";
        Map<Character, Integer> freq2 = countCharacters(text2);
        assert freq2.get('k') == 3 : "k 빈도수 불일치";
        assert freq2.get('a') == 3 : "a 빈도수 불일치";
        assert freq2.get('b') == 1 : "b 빈도수 불일치";
        System.out.println("Test 2 통과 (입력: \"" + text2 + "\") ➔ 결과: " + freq2);

        System.out.println("=== [Day 2-2] CharacterFrequency 모든 검증 완료 (SUCCESS) ===\n");
    }
}
