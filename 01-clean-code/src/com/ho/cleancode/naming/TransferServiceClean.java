package com.ho.cleancode.naming;

/**
 * [클린 코드 스타일]
 * - 다형성(Polymorphism)을 활용하여 if-else를 1줄로 제거
 * - 새 등급이 추가되어도 이 서비스 코드는 단 한 줄도 수정할 필요가 없음
 */
public class TransferServiceClean {

    public double calculateFee(UserGrade grade, double amount) {
        if (grade == null) {
            throw new IllegalArgumentException("사용자 등급은 필수입니다.");
        }
        return grade.calculateFee(amount);
    }
}
