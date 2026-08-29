# 01. 클린 코드 (Clean Code) 핵심 정리

> **"읽기 쉬운 코드가 곧 빠르게 개발하는 유일한 방법이다."** — 로버트 C. 마틴 (Uncle Bob)

---

## 📌 1. 보이스카우트 규칙 (The Boy Scout Rule)
> *"캠프장을 떠날 때는 처음 왔을 때보다 더 깨끗하게 치워놓고 떠나라."*

* 레거시 프로젝트에서 한 번에 전체 코드를 뜯어고치려 하지 마세요.
* 내가 오늘 수정한 **변수명 하나, 50줄짜리 메서드에서 추출한 작은 함수 하나**가 모여 시스템을 건강하게 만듭니다.

---

## 📌 2. 의미 있는 이름 (Meaningful Names)

### ❌ 레거시의 문제
* 의미를 알 수 없는 단문자 변수명 (`u`, `t`, `x`)
* 상태를 알 수 없는 매직 넘버 (`status == 1`, `type == 4`)
* 역할을 짐작하기 어려운 메서드명 (`check()`, `process()`, `getThem()`)

### ⭕ 클린 코드 해결책
* **의도를 명확히 드러내라**: `isEligibleForHighValueTransfer(User user, BigDecimal amount)`
* **매직 넘버를 없애고 Enum이나 상수를 써라**: `UserStatus.ACTIVE`, `UserGrade.VIP`
* **주석 대신 코드로 말하라**: 이름만 보고도 비즈니스 규칙이 전달되도록 설계합니다.

---

## 📌 3. 함수(Function) 작성의 황금률

1. **작게 만들어라**: 함수는 10~15줄 내외로 작을수록 좋습니다.
2. **한 가지만 해라 (Single Responsibility)**: 검증, 계산, 영속화(DB), 알림 발송을 한 함수에 몰아넣지 마세요.
3. **들여쓰기(Indent) 깊이를 줄여라**: 중첩 if-for문을 피하고 **보호 구문(Guard Clause / Early Return)**을 사용하세요.

---

## 📌 4. `Optional<T>`과 Null 방어 원칙

> *"Null을 만든 것은 나의 10억 달러짜리 실수였다."* — 토니 호어 (Tony Hoare)

### 1) `Optional<T>`이란?
* **"값이 들어있을 수도 있고, 비어있을 수도 있는 안전한 상자"**
* `null`을 직접 리턴하지 않고 `Optional<T>`을 리턴하여 호출자에게 "값이 없을 수 있으니 안전하게 꺼내라"고 컴파일 시점에 경고합니다.

### 2) 핵심 활용 패턴 3가지
```java
// 1. 없으면 명확한 예외 발생 (실무 빈도 80%)
User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + id));

// 2. 없으면 기본값 제공
User user = userRepository.findById(id).orElse(User.GUEST);

// 3. 존재할 때만 특정 행위 수행
userRepository.findById(id).ifPresent(u -> System.out.println(u.getName()));
```

### 3) 컬렉션(List/Set/Map) 반환 시 황금률
* 빈 리스트를 반환할 때 **절대 `null`을 반환하지 마세요.**
* `List.of()` 또는 `Collections.emptyList()` 같은 **빈 컬렉션**을 반환하면 호출자에서 `if (list != null)` 체크를 할 필요가 없어집니다.

---

## 🌳 `account` 프로젝트 실무 코드 매핑

| 클린 코드 원칙 | `account` 프로젝트 적용 위치 | 적용 내용 |
| :--- | :--- | :--- |
| **의미 있는 이름 & 불변식** | `tax/core/.../domain/TaxInvoice.java` | `validateAmounts()`, `isPurchaseType()`, `cancel()` |
| **Optional을 통한 Null 방어** | `tax/core/.../service/TaxInvoiceService.java` | `getAPInvoiceById()`가 `Optional<TaxInvoice>` 반환 |
| **명확한 예외 발생** | `tax/core/.../service/TaxInvoiceService.java` | `orElseThrow(() -> new IllegalArgumentException(...))` |

---

## 💻 실습 코드 안내

* [TransferServiceLegacy.java](./src/com/ho/cleancode/naming/TransferServiceLegacy.java) vs [TransferServiceClean.java](./src/com/ho/cleancode/naming/TransferServiceClean.java)
* [InvoiceProcessorLegacy.java](./src/com/ho/cleancode/functions/InvoiceProcessorLegacy.java) vs [InvoiceProcessorClean.java](./src/com/ho/cleancode/functions/InvoiceProcessorClean.java)
* [OptionalPractice.java](./src/com/ho/cleancode/optional/OptionalPractice.java)
