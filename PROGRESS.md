# 📊 Software Craftsmanship & Architecture Study Progress Tracker

> **하성호 엔지니어**의 4개월 마스터 로드맵(2026.09 ~ 2026.12) 세부 진도표입니다.  
> 모든 스터디 세션 완료 시 체크(`[x]`)하고 커밋/푸시하여 실시간으로 진도를 동기화합니다.

---

## 📈 전체 진행률 요약

```text
[Stage 1: 백엔드 모던화 & TDD/리팩터링] ▓▓░░░░░░░░ 20% (3/15)
[Stage 2: 객체지향 설계 & 모던 자바]   ░░░░░░░░░░  0% (0/12)
[Stage 3: 클린/헥사고날 아키텍처 & DDD] ░░░░░░░░░░  0% (0/10)
[Stage 4: 모던 프론트엔드 & 풀스택 연동] ░░░░░░░░░░  0% (0/8)
```

---

## 📚 1단계: 백엔드 모던화 & TDD/리팩터링 (2026.09)

### 1. 『클린 코드 (Clean Code)』 — 로버트 C. 마틴
- [x] **02장. 의미 있는 이름 (Meaningful Names)** `2026-08-29 완료`
  - *실습*: 매직 넘버 제거, 다형성 Enum([`UserGrade.java`](./01-clean-code/src/com/ho/cleancode/naming/UserGrade.java)) 기반 수수료 계산
  - *매핑*: `account` 프로젝트 `TaxInvoice` 도메인 명명 규칙
- [x] **03장. 함수 (Functions)** `2026-08-29 완료`
  - *실습*: 거대 메서드 분해, 단일 책임 원칙(SRP), 보호 구문(Guard Clause / Early Return)
  - *코드*: [`InvoiceProcessorClean.java`](./01-clean-code/src/com/ho/cleancode/functions/InvoiceProcessorClean.java)
- [x] **07장. 오류 처리 (Error Handling) & Null 방어** `2026-08-29 완료`
  - *실습*: `Optional<T>` 3대 핵심 패턴 (`orElseThrow`, `orElse`, `ifPresent`), 컬렉션 `null` 반환 금지
  - *코드*: [`OptionalPractice.java`](./01-clean-code/src/com/ho/cleancode/optional/OptionalPractice.java)
- [ ] **06장. 객체와 자료 구조 (Objects and Data Structures)**
  - 디미터 법칙(Law of Demeter), 자료 은닉 vs 절차적 DTO, 기차 충돌(Train Wreck) 방지
- [ ] **10장. 클래스 (Classes)**
  - 단일 책임 클래스, 높은 응집도(Cohesion)와 낮은 결합도, 변경으로부터 격리
- [ ] **17장. 냄새와 휴리스틱 (Smells and Heuristics)**
  - 경계 조건 캡슐화, 잘못된 위치의 코드(Feature Envy) 탐색

---

### 2. 『리팩터링 2판 (Refactoring 2nd Edition)』 — 마틴 파울러
- [ ] **01장. 리팩터링: 첫 번째 예시**
  - 레거시 공연료 청구서 코드 ➔ 단계 쪼개기(Split Phase) & 다형성 계산기 분리
- [ ] **03장. 코드에서 나는 악취 (24가지 Code Smells)**
  - 기이한 이름, 중복 코드, 긴 함수, 긴 매개변수 목록, 전역 데이터, 가변 데이터, 뒤엉킨 변경 등 24종 식별
- [ ] **06장. 기본적인 리팩터링**
  - 함수 추출하기(Extract Function), 함수 인라인하기, 변수 추출하기/인라인하기, 매개변수 객체 만들기
- [ ] **07장. 캡슐화 (Encapsulation)**
  - 레코드 캡슐화하기, 컬렉션 캡슐화하기, 기본형을 객체로 바꾸기(Primitive Obsession 탈출)
- [ ] **08장. 기능 이동 (Moving Features)**
  - 함수 옮기기(Move Function), 필드 옮기기, 문장을 함수로 옮기기
- [ ] **10장. 조건부 로직 간소화**
  - 조건문 분해하기, 조건식 통합하기, 중첩 조건문을 보호 구문으로 바꾸기, 조건부 로직을 다형성으로 바꾸기
- [ ] **11장. API 리팩터링**
  - 질의 함수와 변경 함수 분리하기(CQS), 매개변수를 질의 함수로 바꾸기, 세터 제거하기

---

### 3. 『테스트 주도 개발 (TDD)』 — 켄트 벡
- [ ] **1부. 화폐 예제 (Money Example - Multi-Currency)**
  - Dollar / Franc 클래스, 가짜 구현(Fake It) ➔ 명백한 구현 ➔ 삼각측량법(Triangulation)
  - 통화(Currency), 환율(Exchange Rate), `Expression` / `Bank` 객체 협력 설계
- [ ] **2부. xUnit 프레임워크 구현**
  - 순수 Java로 간이 테스트 러너 프레임워크 직접 구현해보기
- [ ] **3부. TDD 패턴 및 Red-Green-Refactor 사이클 체화**
  - 테스트할 목록 작성(Todo List), 작은 보폭(Baby Steps), 깨진 유리창 방지

---

### 4. 『단위 테스트 (Unit Testing)』 — 블라디미르 코리코프
- [ ] **좋은 단위 테스트의 4대 요소**
  - 회귀 방지, 리팩터링 내성(False Positive 방지), 빠른 피드백, 유지보수성
- [ ] **목(Mock) vs 스텁(Stub) & 런던파 vs 고전파(Detroit)**
  - 런던파(격리주의, Mock 남용)의 위험성 vs 고전파(상태 검증 중심)
- [ ] **도메인 단위 테스트 & 험블 객체 패턴 (Humble Object)**
  - 비즈니스 로직과 인프라(DB, HTTP)의 완벽 분리

---

## 🏛️ 2단계: 객체지향 설계 & 모던 자바 (2026.10)

### 5. 『오브젝트 (Object)』 — 조영호
- [ ] **01장~03장. 객체, 설계, 역할, 책임, 협력**
  - 절차지향(Theater) ➔ 캡슐화 및 자율적 객체 설계
- [ ] **04장~05장. 책임 할당하기 (GRASP 패턴)**
  - Information Expert, Creator, Low Coupling, High Cohesion, Polymorphism
- [ ] **08장~09장. 의존성 관리 및 유연한 설계**
  - 컴파일타임 의존성 vs 런타임 의존성, 결합도와 유연성, DIP

### 6. 『헤드 퍼스트 디자인 패턴』 — 에릭 프리먼
- [ ] **전략 패턴 (Strategy Pattern)** & **팩토리 메서드 / 추상 팩토리 패턴**
- [ ] **데코레이터 패턴 (Decorator Pattern)** & **어댑터/퍼사드 패턴**
- [ ] **옵저버 / 상태(State) / 템플릿 메서드 패턴**

### 7. 『이펙티브 자바 3판』 — 조슈아 블로크
- [ ] **객체 생성과 파괴** (정적 팩터리 메서드, 빌더 패턴)
- [ ] **모든 객체의 공통 메서드** (`equals`, `hashCode`, 불변식)
- [ ] **클래스와 인터페이스** (불변 객체, 상속보다는 컴포지션)
- [ ] **열거 타입(Enum)과 애너테이션** & **람다와 스트림**

---

## 🏗️ 3단계: 클린/헥사고날 아키텍처 & DDD (2026.11)

### 8. 『만들면서 배우는 클린 아키텍처』 — 톰 홈버그
- [ ] **계층형 아키텍처의 함정** (DB 중심 사고 탈출)
- [ ] **헥사고날 아키텍처(Ports & Adapters) 구조화**
  - Inbound Port(Use Case), Outbound Port, Inbound/Outbound Adapter
- [ ] **도메인 엔티티 vs 영속성 엔티티(JPA) 분리 및 매핑 전략**

### 9. 『클린 아키텍처』 — 로버트 C. 마틴
- [ ] **SOLID 원칙 심화** & **컴포넌트 응집도/결합도 원칙 (REP, CCP, CRP, ADP, SDP, SAP)**
- [ ] **안정된 추상화 원칙과 경계(Boundary) 긋기**

### 10. 『도메인 주도 설계 핵심 (DDD Distilled)』 — 반 버논
- [ ] **유비쿼터스 언어 & Bounded Context**
- [ ] **Aggregate Root, Entity, Value Object(VO) 설계**
- [ ] **도메인 서비스 & 도메인 이벤트(Domain Event) 발행**

---

## 🌐 4단계: 모던 프론트엔드 & 풀스택 완성 (2026.12)

### 11. 『우아한 타입스크립트 with 리액트』 — 우아한형제들
- [ ] **타입스크립트 타입 시스템** (타입 좁히기, 제네릭, 유니온/인터섹션, 불변 타입)
- [ ] **React 19 선언형 UI & Custom Hooks 분리 설계**

### 12. 『실전 Next.js 14/15 프로그래밍』 & 실전 풀스택 연동
- [ ] **Next.js 15 App Router & React Server Components(RSC)**
- [ ] **TanStack Query (React Query) v5 서버 상태 동기화**
- [ ] **`account` 백엔드(Spring Boot 헥사고날) ⟷ 프론트엔드(Next.js) 대시보드 풀스택 완성**
