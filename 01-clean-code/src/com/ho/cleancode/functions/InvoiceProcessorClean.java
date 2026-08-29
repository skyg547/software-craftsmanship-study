package com.ho.cleancode.functions;

/**
 * [클린 코드 스타일 - 작은 함수, 명확한 책임, 보호 구문(Guard Clauses)]
 */
public class InvoiceProcessorClean {

    public void approvePurchaseInvoice(double supplyAmount, String status) {
        validateApprovable(supplyAmount, status);
        
        double tax = calculateTax(supplyAmount);
        double total = supplyAmount + tax;
        
        logApproval(supplyAmount, tax, total);
    }

    private void validateApprovable(double supplyAmount, String status) {
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException("DRAFT(작성중) 상태의 세금계산서만 승인할 수 있습니다.");
        }
        if (supplyAmount <= 0) {
            throw new IllegalArgumentException("공급가액은 0보다 커야 합니다.");
        }
    }

    private double calculateTax(double supplyAmount) {
        return supplyAmount * 0.1;
    }

    private void logApproval(double supply, double tax, double total) {
        System.out.println("[Clean] 매입 세금계산서 승인 완료: 공급가=" + supply + ", 세액=" + tax + ", 합계=" + total);
    }
}
