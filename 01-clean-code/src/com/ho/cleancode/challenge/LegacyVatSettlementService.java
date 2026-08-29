package com.ho.cleancode.challenge;

import java.math.BigDecimal;

/**
 * 🚨 [실전 레거시 문제] 금융/회계 공통코드와 매직 넘버 지옥
 * 
 * 🏢 실무 상황:
 * DB 공통코드 테이블(CMM_CD)을 외우지 않으면 절대 읽을 수 없는 코드입니다.
 * - 세금 구분: "01"(일반과세 10%), "02"(영세율 0%), "03"(면세), "99"(불공제)
 * - 거래 유형: "AP"(매입지급), "AR"(매출수금)
 * - 상태 코드: 10(임시저장), 20(전자세금계산서 승인대기), 30(정산완료), 99(취소)
 * - 매직 넘버: 10000000 (고액 결제 기준선), 0.1 (부가세율)
 */
public class LegacyVatSettlementService {

    // 정산 세액 계산 메서드
    public BigDecimal calc(String cd, String tp, BigDecimal amt, int st) {
        // 상태가 정산완료(30)거나 취소(99)면 처리 불가
        if (st == 30 || st == 99) {
            System.out.println("[Legacy] 정산 불가 상태: " + st);
            return BigDecimal.ZERO;
        }

        // 금액이 없거나 음수면 예외
        if (amt == null || amt.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("ERR_INVALID_AMT");
        }

        BigDecimal vat = BigDecimal.ZERO;

        // 공통코드 "01"은 일반과세(10%)
        if ("01".equals(cd)) {
            vat = amt.multiply(new BigDecimal("0.1"));
            // 1천만원 초과 고액 매입(AP) 건인 경우 특별 가산/수수료 50,000원 추가 (매직 넘버)
            if ("AP".equals(tp) && amt.compareTo(new BigDecimal("10000000")) >= 0) {
                vat = vat.add(new BigDecimal("50000"));
            }
        } 
        // "02"는 영세율(0%), "03"은 면세(0%)
        else if ("02".equals(cd) || "03".equals(cd)) {
            vat = BigDecimal.ZERO;
        } 
        // "99"는 매입세액 불공제(전액 비용처리로 부가세는 0원 처리)
        else if ("99".equals(cd)) {
            vat = BigDecimal.ZERO;
        } else {
            throw new RuntimeException("UNKNOWN_TAX_CODE: " + cd);
        }

        return vat;
    }
}
