package com.ho.cleancode.challenge;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 🌟 [클린 코드] 의도가 명확한 변수명, 보호 구문, Enum 다형성을 적용한 정산 서비스
 */
public class VatSettlementServiceClean {

    /**
     * 세금계산서의 부가세를 계산합니다.
     * 
     * @param taxCategory 과세 구분 (일반과세, 영세, 면세, 불공제)
     * @param transactionType 거래 유형 (매입, 매출)
     * @param supplyAmount 공급가액
     * @param status 현재 정산 상태
     * @return 계산된 부가세액
     */
    public BigDecimal calculateVat(TaxCategory taxCategory,
                                  TransactionType transactionType,
                                  BigDecimal supplyAmount,
                                  SettlementStatus status) {
        // 1. 보호 구문 (Guard Clauses)
        validateInputs(supplyAmount, taxCategory, transactionType, status);

        if (!status.isSettlementAllowed()) {
            System.out.println("[Clean] 이미 정산 완료되었거나 취소된 건입니다: " + status.getDescription());
            return BigDecimal.ZERO;
        }

        // 2. 다형성 Enum에 위임하여 복잡한 if-else 제거
        return taxCategory.calculateVat(supplyAmount, transactionType);
    }

    private void validateInputs(BigDecimal supplyAmount, TaxCategory category, TransactionType type, SettlementStatus status) {
        Objects.requireNonNull(category, "과세 구분은 필수입니다.");
        Objects.requireNonNull(type, "거래 유형은 필수입니다.");
        Objects.requireNonNull(status, "정산 상태는 필수입니다.");

        if (supplyAmount == null || supplyAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("공급가액은 0원보다 커야 합니다. 입력값: " + supplyAmount);
        }
    }
}
