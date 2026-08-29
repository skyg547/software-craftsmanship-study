package com.ho.cleancode;

import com.ho.cleancode.functions.InvoiceProcessorClean;
import com.ho.cleancode.functions.InvoiceProcessorLegacy;
import com.ho.cleancode.naming.TransferServiceClean;
import com.ho.cleancode.naming.TransferServiceLegacy;
import com.ho.cleancode.naming.UserGrade;
import com.ho.cleancode.optional.OptionalPractice;

public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("🚀 [01-clean-code] 클린 코드 핵심 원칙 실습 데모");
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

        System.out.println("\n==================================================");
        System.out.println("✅ 모든 클린 코드 데모가 성공적으로 실행되었습니다!");
        System.out.println("==================================================");
    }
}
