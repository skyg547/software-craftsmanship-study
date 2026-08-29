package com.ho.cleancode.naming;

/**
 * [레거시 스타일]
 * - 거대한 if-else 분기
 * - 새로운 등급이 생길 때마다 이 메서드를 계속 수정해야 함 (버그 유발 위험)
 * - 문자열(String) 비교로 인한 오타 런타임 에러 가능성
 */
public class TransferServiceLegacy {

    public double calculateFee(String userGrade, double amount) {
        double fee = 0.0;

        if ("VIP".equals(userGrade)) {
            fee = 0.0;
        } else if ("GOLD".equals(userGrade)) {
            fee = amount * 0.015;
        } else if ("SILVER".equals(userGrade)) {
            fee = amount * 0.03;
        } else if ("BASIC".equals(userGrade)) {
            fee = 500.0;
        } else {
            throw new IllegalArgumentException("알 수 없는 등급입니다: " + userGrade);
        }

        return fee;
    }
}
