package com.ho.study.jvm;

/**
 * Day 2 금융 실습: 불변 객체(Immutable Object)와 오버플로우 방어
 * 
 * [금융 시스템 핵심 원칙]
 * 1. 가변 Money 객체는 외부 공유 시 잔액 오염 및 동시성 버그를 유발합니다.
 * 2. 불변 Record Money는 상태 변경 시 항상 새로운 객체를 반환하여 사이드 이펙트를 원천 차단합니다.
 * 3. `Math.addExact`를 사용하여 long 범위를 초과하는 금융 오버플로우를 즉시 감지합니다.
 */
public final class ImmutableMoneyExample {

    private ImmutableMoneyExample() {
    }

    public record Money(long amount) {

        public Money {
            if (amount < 0) {
                throw new IllegalArgumentException("금액은 음수일 수 없습니다: " + amount);
            }
        }

        public static Money of(long amount) {
            return new Money(amount);
        }

        public Money add(Money other) {
            // ArithmeticException 발생 시 오버플로우 즉시 차단
            return new Money(Math.addExact(this.amount, other.amount));
        }

        public Money subtract(Money other) {
            if (this.amount < other.amount) {
                throw new IllegalStateException("출금 실패: 잔액이 부족합니다. (현재: " + this.amount + ", 요청: " + other.amount + ")");
            }
            return new Money(this.amount - other.amount);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== [Day 2 금융] 불변 Money & 오버플로우 방어 실습 시작 ===");

        Money initialBalance = Money.of(100_000L);
        Money deposit = Money.of(50_000L);

        // 연산 후 새로운 객체가 반환되며, initialBalance는 절대 변경되지 않음
        Money updatedBalance = initialBalance.add(deposit);

        System.out.println("initialBalance : " + initialBalance.amount() + "원 (불변 유지!)");
        System.out.println("updatedBalance : " + updatedBalance.amount() + "원");
        assert initialBalance.amount() == 100_000L : "원본 balance가 오염되었습니다!";
        assert updatedBalance.amount() == 150_000L : "계산 결과 불일치";

        // 오버플로우 방어 검증
        try {
            Money huge = Money.of(Long.MAX_VALUE);
            huge.add(Money.of(1L)); // 오버플로우 시도
            assert false : "오버플로우 예외가 발생해야 합니다.";
        } catch (ArithmeticException e) {
            System.out.println("금융 오버플로우 방어 성공 ➔ Math.addExact로 ArithmeticException 포착");
        }

        System.out.println("=== [Day 2 금융] 불변 Money 실습 성공 (SUCCESS) ===\n");
    }
}
