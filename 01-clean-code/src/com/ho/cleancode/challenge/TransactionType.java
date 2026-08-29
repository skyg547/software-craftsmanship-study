package com.ho.cleancode.challenge;

public enum TransactionType {
    PURCHASE_PAYABLE("AP", "매입 지급"),
    SALES_RECEIVABLE("AR", "매출 수금");

    private final String legacyCode;
    private final String description;

    TransactionType(String legacyCode, String description) {
        this.legacyCode = legacyCode;
        this.description = description;
    }

    public String getLegacyCode() { return legacyCode; }
    public String getDescription() { return description; }

    public static TransactionType fromLegacyCode(String code) {
        if ("AP".equalsIgnoreCase(code)) return PURCHASE_PAYABLE;
        if ("AR".equalsIgnoreCase(code)) return SALES_RECEIVABLE;
        throw new IllegalArgumentException("지원하지 않는 거래유형 코드입니다: " + code);
    }
}
