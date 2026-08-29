package com.ho.cleancode.naming;

/**
 * [클린 코드 - 스스로 비즈니스 계산 책임을 갖는 Enum]
 * 새로운 등급이 추가되더라도 기존 코드를 수정하지 않고(Closed), 
 * 새 Enum 상수를 추가(Open)하여 OCP(개방-폐쇄 원칙)를 만족합니다.
 */
public enum UserGrade {

    VIP {
        @Override
        public double calculateFee(double amount) {
            return 0.0; // VIP는 면제
        }
    },
    GOLD {
        @Override
        public double calculateFee(double amount) {
            return amount * 0.015; // 1.5% 우대 수수료
        }
    },
    SILVER {
        @Override
        public double calculateFee(double amount) {
            return amount * 0.03; // 3.0% 수수료
        }
    },
    BASIC {
        @Override
        public double calculateFee(double amount) {
            return 500.0; // 기본 500원 고정 수수료
        }
    };

    public abstract double calculateFee(double amount);
}
