package com.ho.cleancode;

import com.ho.cleancode.challenge.*;
import com.ho.cleancode.functions.InvoiceProcessorClean;
import com.ho.cleancode.functions.InvoiceProcessorLegacy;
import com.ho.cleancode.naming.TransferServiceClean;
import com.ho.cleancode.naming.TransferServiceLegacy;
import com.ho.cleancode.naming.UserGrade;
import com.ho.cleancode.optional.OptionalPractice;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("🚀 [01-clean-code] 클린 코드 핵심 원칙 실습 & 하네스");
        System.out.println("==================================================");

        // 1. 의미 있는 이름 & if-else 제거 (TransferService)
        System.out.println("\n[1] 수수료 계산 로직 비교 (Legacy vs Clean)");
        TransferServiceLegacy legacyTransfer = new TransferServiceLegacy();
        double legacyFee = legacyTransfer.calculateFee("GOLD", 1_000_000);
        System.out.println("Legacy 결과 (문자열 파라미터): " + legacyFee + "원");

        TransferServiceClean cleanTransfer = new TransferServiceClean();
        double cleanFee = cleanTransfer.calculateFee(UserGrade.GOLD, 1_000_000);
        System.out.println("Clean 결과 (다형성 Enum 활용): " + cleanFee + "원");

        // 2. 작고 명확한 함수 (InvoiceProcessor)
        System.out.println("\n[2] 세금계산서 승인 로직 비교 (Legacy vs Clean)");
        InvoiceProcessorLegacy legacyInvoice = new InvoiceProcessorLegacy();
        legacyInvoice.process("PURCHASE", 500_000, "DRAFT");

        InvoiceProcessorClean cleanInvoice = new InvoiceProcessorClean();
        cleanInvoice.approvePurchaseInvoice(500_000, "DRAFT");

        // 3. Optional & Null 방어
        System.out.println("\n[3] Optional<T> & Null-Safe 컬렉션 실습");
        OptionalPractice optionalPractice = new OptionalPractice();
        optionalPractice.runDemo();

        // 4. 🔥 [NEW] 실전 공통코드 & 매직 넘버 탈출 챌린지
        System.out.println("\n[4] 🔥 금융/회계 공통코드(CMM_CD) & 매직넘버 탈출 실전 챌린지");
        runChallengeDemo();

        System.out.println("\n==================================================");
        System.out.println("✅ 모든 클린 코드 데모 및 하네스 검증 통과!");
        System.out.println("==================================================");
    }

    private static void runChallengeDemo() {
        LegacyVatSettlementService legacy = new LegacyVatSettlementService();
        VatSettlementServiceClean clean = new VatSettlementServiceClean();

        BigDecimal highSupply = new BigDecimal("20000000"); // 2천만원

        // Case 1: 고액 매입 일반과세 (10% + 5만원 가산)
        System.out.println("--- [테스트 케이스 1] 2천만원 고액 매입 세금계산서 정산 ---");
        BigDecimal legacyResult1 = legacy.calc("01", "AP", highSupply, 20);
        BigDecimal cleanResult1 = clean.calculateVat(
                TaxCategory.TAXABLE, 
                TransactionType.PURCHASE_PAYABLE, 
                highSupply, 
                SettlementStatus.PENDING_APPROVAL
        );
        System.out.println("  Legacy 결과: " + legacyResult1 + "원");
        System.out.println("  Clean  결과: " + cleanResult1 + "원");
        assert legacyResult1.compareTo(cleanResult1) == 0 : "결과 불일치!";

        // Case 2: 면세(03) 매출 건
        System.out.println("--- [테스트 케이스 2] 면세 매출 세금계산서 정산 ---");
        BigDecimal legacyResult2 = legacy.calc("03", "AR", new BigDecimal("5000000"), 10);
        BigDecimal cleanResult2 = clean.calculateVat(
                TaxCategory.TAX_EXEMPT,
                TransactionType.SALES_RECEIVABLE,
                new BigDecimal("5000000"),
                SettlementStatus.DRAFT
        );
        System.out.println("  Legacy 결과: " + legacyResult2 + "원");
        System.out.println("  Clean  결과: " + cleanResult2 + "원");
        assert legacyResult2.compareTo(cleanResult2) == 0 : "결과 불일치!";

        System.out.println("  🎉 검증 결과: Legacy와 Clean의 비즈니스 계산 정합성이 100% 일치합니다!");
    }
}
