package com.ho.study.jvm;

/**
 * Day 2 JVM 실습: 참조(Reference) 복사의 함정과 객체 생성
 * 
 * [핵심 원리]
 * 1. `Account second = first;`
 *    -> 객체가 힙에 새로 복사되는 것이 아니라, 스택에 있는 참조 주소(Reference)만 복사됨!
 *    -> second를 통해 withdraw(30_000)하면 first의 balance도 함께 줄어듦 (동일 객체 가리킴)
 * 2. `Account third = new Account(first.balance());`
 *    -> 힙에 완전히 새로운 객체 인스턴스가 독립적으로 할당됨!
 */
public final class ReferenceExample {

    private ReferenceExample() {
    }

    public static void main(String[] args) {
        System.out.println("=== [Day 2 JVM] 참조 복사 vs 객체 생성 실습 시작 ===");

        // 1. 초기 계좌 생성 (잔액 100,000원)
        Account first = new Account(100_000L);

        // 2. 참조 복사 (Reference Copy)
        Account second = first;

        // second에서 30,000원 출금
        second.withdraw(30_000L);

        System.out.println("[참조 복사 후]");
        System.out.println("first.balance()  : " + first.balance() + "원 (first도 함께 출금됨!)");
        System.out.println("second.balance() : " + second.balance() + "원");
        assert first.balance() == 70_000L : "first 잔액 불일치";
        assert second.balance() == 70_000L : "second 잔액 불일치";
        assert first == second : "first와 second는 물리적으로 동일한 참조 주소여야 합니다.";

        // 3. 완전히 독립된 신규 객체 생성 (New Instance)
        Account third = new Account(first.balance());
        third.withdraw(20_000L);

        System.out.println("\n[독립 객체 생성 후 (third.withdraw(20,000))]");
        System.out.println("first.balance() : " + first.balance() + "원 (영향 받지 않음)");
        System.out.println("third.balance() : " + third.balance() + "원");
        assert first.balance() == 70_000L : "first 잔액 오염됨!";
        assert third.balance() == 50_000L : "third 잔액 불일치";
        assert first != third : "first와 third는 서로 다른 힙 주소를 가져야 합니다.";

        System.out.println("=== [Day 2 JVM] 참조 복사 실습 성공 (SUCCESS) ===\n");
    }

    public static final class Account {
        private long balance;

        public Account(long balance) {
            this.balance = balance;
        }

        public void withdraw(long amount) {
            if (amount > balance) {
                throw new IllegalStateException("잔액이 부족합니다.");
            }
            this.balance -= amount;
        }

        public long balance() {
            return balance;
        }
    }
}
