package com.ho.cleancode.challenge;

public enum SettlementStatus {
    DRAFT(10, "임시저장"),
    PENDING_APPROVAL(20, "승인대기"),
    SETTLED(30, "정산완료"),
    CANCELLED(99, "취소");

    private final int statusCode;
    private final String description;

    SettlementStatus(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public boolean isSettlementAllowed() {
        return this != SETTLED && this != CANCELLED;
    }

    public int getStatusCode() { return statusCode; }
    public String getDescription() { return description; }

    public static SettlementStatus fromStatusCode(int code) {
        for (SettlementStatus status : values()) {
            if (status.statusCode == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 정산 상태 코드입니다: " + code);
    }
}
