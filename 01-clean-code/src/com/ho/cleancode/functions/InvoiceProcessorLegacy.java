package com.ho.cleancode.functions;

/**
 * [레거시 스타일 - 한 함수가 5가지 일을 다 하는 비대함]
 */
public class InvoiceProcessorLegacy {

    public void process(String type, double supplyAmount, String status) {
        // 중첩 if문 지옥 (들여쓰기 3단계)
        if (type != null) {
            if ("PURCHASE".equals(type)) {
                if ("DRAFT".equals(status)) {
                    if (supplyAmount > 0) {
                        double tax = supplyAmount * 0.1;
                        double total = supplyAmount + tax;
                        System.out.println("[Legacy] 매입 세금계산서 승인 완료: 공급가=" + supplyAmount + ", 세액=" + tax + ", 합계=" + total);
                    } else {
                        throw new IllegalArgumentException("금액 오류");
                    }
                }
            }
        }
    }
}
