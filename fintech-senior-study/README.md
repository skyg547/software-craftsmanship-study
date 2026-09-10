# 💳 핀테크/빅테크 Senior Backend 마스터 스터디

> **목표**: 2027년 1분기 토스뱅크 · 카카오뱅크 · 네이버파이낸셜 · 쿠팡페이급 Senior Backend 이직 완성  
> **기간**: 2026.09.08 ~ 2027.03.31 (총 29주 일일 집중 커리큘럼)  
> **핵심 페르소나**: 6년 차 금융/회계 백엔드 엔지니어의 **"코드 구현자" ➔ "대규모 분산 시스템 설계자"** 도약

---

## 🎯 1. 6년 차 엔지니어 역량 진단 및 시니어 전환 전략

### 🌟 현재 보유한 핵심 강점
* **자금운용 / 회계원장 / 대규모 배치 실무 내공**: 은행권 자금 흐름, `BigDecimal` 1원 단위 정합성, 대용량 트랜잭션 및 배치 처리 이해도 보유
* **현업 이슈에 대한 높은 설계 감각**: Gradle 72개 멀티모듈, 16개+ MSA 서비스 간 의존성, Jib 도입 등 실무 고민의 수준이 이미 Mid-level 이상

### 🚀 Senior 합격을 위해 보완할 3대 핵심 영역
1. **파편화된 지식의 연결 (End-to-End 연결 고리)**:
   * Java 코드가 Bytecode ➔ ClassLoader ➔ JVM 메모리(Stack/Heap) ➔ JIT Native Code ➔ OS Thread ➔ Docker/K8s로 연결되는 전체 실행 파이프라인 체득
2. **트레이드오프(Trade-off) 중심의 의사결정 설명력**:
   * "왜 A 대신 B를 썼는가?"에 대해 성능, 유지보수성, 복잡도, 실패 격리 관점에서 명확한 근거 제시
3. **손으로 직접 구현하는 알고리즘 & 자료구조 감각**:
   * 눈으로만 보는 코딩테스트 탈피 ➔ 요구사항 분석 ➔ 최적 자료구조 선택 ➔ Clean Java 구현 ➔ 면접 스크립트까지 완결

---

## 🏛️ 2. MSA 멀티모듈 & 컨테이너 빌드 아키텍처 가이드

대규모 Gradle 멀티모듈(72개 서브프로젝트, 16개+ MSA) 환경에서 흔히 겪는 **중복 Dockerfile, 느린 CI/CD 빌드, 서비스 간 결합도 문제**를 해결하는 모범 아키텍처입니다.

### 2.1. Jib vs bootBuildImage vs Dockerfile 선택 기준

```text
               ┌──────────────────────────────┐
               │     모듈 유형별 컨테이너 전략    │
               └──────────────┬───────────────┘
                              │
       ┌──────────────────────┼──────────────────────┐
       ▼                      ▼                      ▼
[표준 JVM MSA]          [특수 OS 조작 필요]      [프론트엔드 (Next.js)]
   • Jib (80~90%)         • Containerfile (10%)    • Dockerfile
   • Docker 데몬 불필요      • C 라이브러리, 폰트      • Multi-stage Build
   • 클래스/의존성 레이어 분리   • Escape Hatch 유지      • 독립 캐싱 유지
```

* **JVM MSA의 표준 = Google Jib**: Docker 데몬 없이 레이어별(`classes`, `resources`, `dependencies`, `snapshot-dependencies`) 증분 빌드 수행. 소스 코드 한 줄 변경 시 수백 MB 라이브러리를 재빌드/재전송하지 않음.
* **특수 서비스 = Containerfile Escape Hatch**: 네이티브 C 라이브러리 의존성, 폰트, 특수 바이너리가 필요한 경우에만 예외적으로 공통 Containerfile 베이스 사용.
* **로컬 개발 실행 = Docker Compose 중앙화**: 이미지 빌드 도구와 로컬 통합 오케스트레이션(DB, Kafka, Redis, Zipkin 등)을 엄격히 분리.

### 2.2. `shared-kernel` vs `contract` vs `common` 모듈 분리 기준

> **핵심 원칙**: "실행되는 것(API/Batch)과 재사용되는 라이브러리는 완전히 격리한다."

```text
account/
├── build-logic/                # 빌드 컨벤션 플러그인 (의존성 일원화)
│   ├── java-library-conventions
│   └── spring-service-conventions
├── shared/
│   ├── shared-kernel/          # 초경량 도메인 기본 타입 (Money, Currency, BaseVO)
│   ├── common-web/             # GlobalExceptionHandler, ResultEnvelope
│   ├── common-security/        # JWT, 인증/인가 필터
│   └── common-observability/   # OpenTelemetry, Micrometer Tracing
├── contracts/                  # 서비스 간 비동기/동기 통신 계약
│   ├── transfer-event-contract # Kafka 메시지 스키마
│   └── settlement-dto-contract # Feign/HTTP DTO
└── services/                   # 독립 배포 가능한 최종 실행 서비스 (Jib 적용)
    ├── account-api
    ├── transfer-api
    └── settlement-batch
```

* **Shared Kernel은 극도로 작게**: 스프링 의존성 없이 순수 Java(`java-library`)로 작성. 비즈니스 로직이 들어가선 안 됨.
* **MSA 독립성과 중앙화 균형**: 루트 `build.gradle`의 `subprojects { ... }`로 모든 버전을 강제 결합하지 말고, `build-logic` 또는 Gradle Version Catalog를 통해 **"정책은 중앙 관리하되 각 서비스는 독립된 버전을 점진적으로 업그레이드"** 가능하도록 설계.

---

## 🗺️ 3. 29주 핀테크 시니어 커리큘럼 로드맵

```text
[1~4주차]  : Java 심층 원리 & JVM & 메모리/GC & 코딩테스트 기초체력 (Two Sum, Set/Map)
[5~8주차]  : Spring Boot 내부 원리, DB 격리수준, 트랜잭션 전파, 비관적/낙관적 락
[9~12주차] : DDD, Bounded Context, Aggregates, 멀티모듈 경계 설계
[13~16주차]: MSA 분산 트랜잭션 (SAGA, Outbox Pattern), 분산 락 (Redis Redlock)
[17~20주차]: Kafka 이벤트 기반 아키텍처, 멱등성(Idempotency), 메시지 순서 보장
[21~24주차]: CI/CD 파이프라인, Jib/Docker, Kubernetes 오케스트레이션, 무중단 배포
[25~27주차]: Observability (Tracing, Metrics, Logging), 대규모 시스템 설계 (System Design)
[28~29주차]: 금융 도메인 특화 STAR 이력서 완성 & 라이브 코딩테스트/모의면접 최종 대비
```

---

## 📂 4. 일일 스터디 노트 및 실습 목차

| 일차 | 학습 주제 | 핵심 내용 | 문서 & 코드 링크 |
| :---: | :--- | :--- | :--- |
| **Day 1** | **Java 실행 메커니즘** | JDK/JVM 차이, Bytecode, ClassLoader 3단계, JIT C1/C2 컴파일, Tiered Compilation, OSR, Escape Analysis, JVM Warm-up | • [Day 1 스터디 노트](./day01-jvm-bytecode-jit.md)<br/>• [Two Sum 알고리즘 해설](./algorithms/day01-two-sum.md)<br/>• [`TwoSum.java`](./src/com/ho/study/algorithm/day01/TwoSum.java) |
| **Day 2** | **JVM 메모리 & GC 출발점** | Stack Frame, Heap(Eden/Old), Metaspace, 참조(Reference) 복사의 함정, 금융 불변 Record, GC Root & Reachability | • [Day 2 스터디 노트](./day02-jvm-memory-gc.md)<br/>• [`ReferenceExample.java`](./src/com/ho/study/jvm/ReferenceExample.java)<br/>• [`DuplicateFinder.java`](./src/com/ho/study/algorithm/day02/DuplicateFinder.java)<br/>• [`CharacterFrequency.java`](./src/com/ho/study/algorithm/day02/CharacterFrequency.java) |
| **Day 3** | **GC 심화, 세대분리 & Two Pointer** | Young/Old 분리(세대 가설), Eden/Survivor, Stop-The-World, P99 Tail Latency, Throughput vs Latency, Two Pointer 최적화 | • [Day 3 스터디 노트](./day03-gc-generation-stw.md)<br/>• [Two Pointer 알고리즘 해설](./algorithms/day03-two-pointer.md)<br/>• [`SortedTwoSum.java`](./src/com/ho/study/algorithm/day03/SortedTwoSum.java)<br/>• [`PalindromeChecker.java`](./src/com/ho/study/algorithm/day03/PalindromeChecker.java) |


---

## 🏃 5. 실습 코드 실행 안내

모든 코드는 외부 의존성 없이 표준 Java 17+ 환경에서 즉시 컴파일 및 검증 가능합니다.

```bash
# 전체 스터디 및 하네스 실행 (01-clean-code + fintech-senior-study)
./run.sh
```
