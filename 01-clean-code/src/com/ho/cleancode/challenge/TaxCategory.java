package com.ho.cleancode.challenge;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 🌟 [클린 코드] 공통코드 "01", "02", "03", "99"를 스스로 세액 계산 책임을 지닌 Enum으로 승격
 */
public enum TaxCategory {
    TAXABLE("01", "일반과세 (10%)", new BigDecimal("0.10")) {
        @Override
        public BigDecimal calculateVat(BigDecimal supplyAmount, TransactionType transactionType) {
            BigDecimal standardVat = supplyAmount.multiply(getTaxRate());
            // 고액 매입(AP) 추가 수수료/부가세 가산 정책
            if (transactionType == TransactionType.PURCHASE_PAYABLE 
                    && supplyAmount.compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
                return standardVat.add(HIGH_VALUE_SURCHARGE);
            }
            return standardVat;
        }
    },
    ZERO_RATED("02", "영세율 (0%)", BigDecimal.ZERO) {
        @Override
        public BigDecimal calculateVat(BigDecimal supplyAmount, TransactionType transactionType) {
            return BigDecimal.ZERO;
        }
    },
    TAX_EXEMPT("03", "면세 (0%)", BigDecimal.ZERO) {
        @Override
        public BigDecimal calculateVat(BigDecimal supplyAmount, TransactionType transactionType) {
            return BigDecimal.ZERO;
        }
    },
    NON_DEDUCTIBLE("99", "매입세액 불공제", BigDecimal.ZERO) {
        @Override
        public BigDecimal calculateVat(BigDecimal supplyAmount, TransactionType transactionType) {
            return BigDecimal.ZERO;
        }
    };

    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("10000000"); // 1천만원
    private static final BigDecimal HIGH_VALUE_SURCHARGE = new BigDecimal("50000");    // 5만원

    private final String legacyCode;
    private final String description;
    private final BigDecimal taxRate;

    TaxCategory(String legacyCode, String description, BigDecimal taxRate) {
        this.legacyCode = legacyCode;
        this.description = description;
        this.taxRate = taxRate;
    }

    public abstract BigDecimal calculateVat(BigDecimal supplyAmount, TransactionType transactionType);

    public String getLegacyCode() { return legacyCode; }
    public String getDescription() { return description; }
    public BigDecimal getTaxRate() { return taxRate; }

    /**
     * 레거시 DB 문자열 코드("01", "02" 등)를 타입 안전한 Enum으로 변환 (보호 구문)
     */
    public static TaxCategory fromLegacyCode(String code) {
        return Arrays.stream(values())
                .filter(c -> c.legacyCode.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 과세 공통코드입니다: " + code));
    }
}
