# Day 3 — 가비지 컬렉션(GC), 세대 분리(Young/Old), Stop-The-World

> **학습 목표**: JVM 가비지 컬렉션(GC)의 Reachability 원리와 약한 세대 가설(Weak Generational Hypothesis)을 이해하고, Stop-The-World(STW)가 금융 결제/송금 API의 P99 지연시간(Tail Latency)에 미치는 영향을 운영자의 시각에서 설명할 수 있다.

---

## 🧭 1. 전체 실행 흐름도 (객체 라이프사이클과 GC)

```text
[ 비즈니스 로직 실행 ]
  Account account = new Account();
         │
         ▼
[ Heap 메모리 할당 (Eden) ]
  • 객체 생성률(Allocation Rate) 증가
  • 대부분의 객체(DTO, 임시 Command)는 메서드 종료 후 Unreachable 전환
         │
         ▼
[ Eden 영역 포화 ➔ Young GC (Minor GC) 트리거 ]
  ┌──────────────────────────────────────────────────────────┐
  │ 1. GC Root 식별 (스레드 스택 프레임, Metaspace static 참조)   │
  │ 2. Stop-The-World (STW): 애플리케이션 스레드 일시 중단       │
  │ 3. Reachable 객체 탐색 (Mark) ➔ Survivor 영역으로 복사 (Copy) │
  │ 4. 죽은 객체 일괄 수거 (Sweep) ➔ 애플리케이션 스레드 재개    │
  └────────────────────────┬─────────────────────────────────┘
                           │ 지속 생존 (Age Threshold 초과)
                           ▼
[ Old Generation 승격 (Promotion) ]
  • 싱글톤 빈, 장기 캐시, 영속 컬렉션 보관
  • Old 영역 포화 시 Major GC / Full GC 발생 위험 (긴 STW)
```

---

## 🔍 2. 전문 설명 & 초보자 직관 해석 (2단 구조)

### 2.1. Garbage Collection & Reachability
* **전문 설명**: C/C++처럼 명시적인 메모리 해제(`free`) 없이, JVM이 GC Root로부터 참조 체인을 추적하여 도달 불가능(Unreachable)한 고립 객체를 자동으로 감지하고 메모리를 회수하는 메커니즘.
* **초보자 직관 해석**:
  > **GC는 '창고 청소 로봇'입니다.**  
  > 개발자는 방(Heap)에 물건(객체)을 들여놓고 쓰기만 합니다. 책상(Stack)에 적어둔 주소 포스트잇(Reference)을 구겨서 버리면, 청소 로봇이 "아무도 이 물건을 찾을 수 없구나!"라고 판단하여 재활용 수거함으로 가져갑니다.

### 2.2. 약한 세대 가설 (Weak Generational Hypothesis)
* **전문 설명**: 대다수의 객체는 생성 후 매우 짧은 시간 내에 비도달 상태가 되며, 오래된 객체에서 새로운 객체로의 참조는 극히 드물다는 실증적 통계 법칙. 이에 따라 힙을 Young과 Old로 물리적/논리적으로 분할합니다.
* **초보자 직관 해석**:
  > **"신입 사원 대기실(Young)과 장기 근속 임원실(Old)"**  
  > 임시 메모지나 종이컵(DTO, 쿼리 결과)은 90% 이상 1분 안에 버려집니다. 그래서 전체 건물을 매번 대청소하지 않고, 쓰레기가 쏟아지는 신입 대기실(Young)만 1초마다 가볍게 청소(Young GC)하는 것이 훨씬 효율적입니다.

### 2.3. Stop-The-World (STW)
* **전문 설명**: 가비지 컬렉터가 객체 간의 참조 그래프를 안전하고 일관되게 분석(Mark)하거나 메모리를 재배치(Compact)하기 위해 모든 애플리케이션 스레드의 실행을 일시 정지시키는 현상.
* **초보자 직관 해석**:
  > **"은행 영업점 청소 시간 창구 일시 중단"**  
  > 고객들이 계속 돈을 입출금하며 장부를 바꾸면 청소부가 정확한 재고 조사를 할 수 없습니다. 그래서 "잠시만 멈춰주세요!" 외치고 모든 창구를 닫은 뒤 정리하고 다시 문을 여는 것입니다.

---

## 🧱 3. Young Generation 내부 구조와 Promotion 메커니즘

```text
Young Generation                                             Old Generation
┌──────────────────────────────────────┐                     ┌────────────────────────┐
│ Eden (신규 객체)                      │                     │                        │
│ ┌──────────────────────────────────┐ │                     │ Application Cache      │
│ │ new Order(), new TransferDto()   │ │                     │ Spring Singletons      │
│ └──────────────────────────────────┘ │   Tenuring Age > 15 │ DB Connection Pools    │
│ Survivor 0 (S0)   Survivor 1 (S1)    │ ──────────────────▶ │                        │
│ ┌───────────────┐ ┌────────────────┐ │   (Promotion)       │                        │
│ │ Age=1, Age=2  │ │  (비어 있음)    │ │                     │                        │
│ └───────────────┘ └────────────────┘ │                     │                        │
└──────────────────────────────────────┘                     └────────────────────────┘
```

1. **Eden**: `new` 연산자로 생성된 객체가 최초로 진입하는 공간.
2. **Survivor 0 / Survivor 1 (From / To)**:
   * Eden이 가득 차면 Young GC가 발생합니다.
   * 살아남은 객체는 Survivor 영역 중 한 곳으로 복사되고, `age` 카운터가 1 증가합니다.
   * **원칙**: 두 Survivor 영역 중 **하나는 반드시 비어 있어야** 합니다 (단편화 방지를 위한 Copying 알고리즘).
3. **Old Generation 승격 (Promotion)**:
   * 여러 번의 Young GC를 버텨 `age`가 임계치(`-XX:MaxTenuringThreshold`, 기본 15)를 초과하면 Old Generation으로 승격됩니다.

---

## ⏱️ 4. 금융 시스템 실무: Throughput vs Latency & P99 Tail Latency

### 4.1. 평균(Average)의 함정과 Tail Latency
금융 결제/송금 API 모니터링 시 평균 응답 시간만 보면 장애를 감지할 수 없습니다.

```text
평균 응답 속도 : 30ms (지극히 정상으로 보임)
P50 (중앙값)   : 20ms
P95            : 45ms
P99            : 1,800ms (1.8초! ➔ 결제 타임아웃 발생 위험)
```

* **원인**: 100건 중 99건은 30ms에 끝나지만, 하필 1건이 수행되는 도중 발생한 **수백 ms~수초의 GC Stop-The-World (STW)**가 결려 P99 지연시간이 폭증한 것입니다.
* **운영 경험의 차이**:
  * *주니어*: "DB 쿼리가 느린가? 네트워크 문제인가?"
  * *시니어*: "APM에서 P99 튄 시점의 **GC Pause 시간, Heap 사용량 추이, Allocation Rate, JVM SafePoint 대기 시간**"을 즉각 대조.

### 4.2. Throughput vs Latency 트레이드오프
| 구분 | 처리량 중심 (Throughput) | 저지연 중심 (Low Latency) |
| :--- | :--- | :--- |
| **목표** | 단위 시간당 처리하는 총 작업량 극대화 | 개별 요청이 완료되는 응답 시간(Pause) 최소화 |
| **적합 업무** | 야간 이자 정산 배치, 대량 결제 데이터 집계 | 실시간 계좌이체, 간편결제 승인, 증권 호가 주문 |
| **대표 GC** | **Parallel GC** | **G1 GC**, **ZGC** (Pause < 1ms 목표) |
| **특징** | STW가 발생하더라도 한 번에 크게 처리 | 백그라운드 스레드로 동시(Concurrent) 작업 수행, CPU 사용률 약간 증가 |

---

## ⚖️ 5. "객체 생성 = 나쁨?" 조기 최적화(Premature Optimization)의 경계

```java
public Money calculateFee(Money amount) {
    Money rate = new Money(10);
    return amount.multiply(rate);
}
```

* **오해**: "객체를 생성하면 GC 압력이 올라가니 전부 `long` 원시값으로 바꿔야 한다!"
* **시니어의 정답**:
  > "JVM의 힙 할당(TLAB - Thread-Local Allocation Buffer)과 JIT 탈출 분석(스택 할당/스칼라 치환), Young GC 수거 효율은 상상 이상으로 뛰어납니다.  
  > 가독성과 도메인 불변식(음수 방지, 원 단위 정합성)을 희생하면서까지 원시 타입을 고집하는 것은 잘못된 **조기 최적화**입니다.  
  > **'안전하고 명확한 도메인 코드 작성 ➔ 운영 지표 측정 ➔ 실제 Allocation 병목 확인 시 최적화'** 순서가 정답입니다."

---

## 🎤 6. 시니어 백엔드 면접 실전 질문 5선

### Q1. Java GC는 어떤 기준으로 객체를 메모리에서 제거하나요?
> **답변 핵심**:  
> "참조 변수가 `null`이 되었다고 즉시 수거되는 것이 아니라, 실행 중인 스레드의 스택 프레임, Metaspace의 정적 변수, JNI 참조와 같은 **GC Root로부터 참조 체인을 따라 도달 불가능(Unreachable)한 상태**가 된 객체를 수거 대상으로 판정합니다."

### Q2. 힙(Heap) 메모리를 Young Generation과 Old Generation으로 분리하는 이유는 무엇인가요?
> **답변 핵심**:  
> "대다수의 객체는 생성 직후 짧은 시간 내에 소멸한다는 **약한 세대 가설(Weak Generational Hypothesis)**에 근거합니다. 전체 힙을 한 번에 검사하지 않고, 수명이 짧은 객체가 모인 Young 영역만 경량화된 Minor GC(Copying 알고리즘)로 빠르게 수거함으로써 GC 오버헤드를 극적으로 줄입니다."

### Q3. Stop-The-World (STW)란 무엇이며 시스템에 어떤 영향을 주나요?
> **답변 핵심**:  
> "GC 작업 동안 객체 참조 관계의 일관성을 유지하기 위해 JVM이 애플리케이션의 모든 사용자 스레드를 일시 정지시키는 현상입니다. STW 시간이 길어지면 시스템의 평균 속도는 양호하더라도 P99, P99.9 같은 Tail Latency가 튀어 실시간 금융 결제에서 타임아웃 장애를 유발할 수 있습니다."

### Q4. GC 튜닝 시 Throughput과 Latency의 상충 관계(Trade-off)를 설명해주세요.
> **답변 핵심**:  
> "Throughput 중심 GC(예: Parallel GC)는 전체 애플리케이션 가동 시간을 극대화하기 위해 배치성 STW를 감수하며, Latency 중심 GC(예: ZGC, G1 GC)는 사용자의 체감 응답 시간을 줄이기 위해 GC 작업을 백그라운드 스레드로 동시(Concurrent) 수행하여 STW를 수 밀리초 이하로 억제하지만 일정 수준의 추가 CPU 리소스를 소모합니다."

### Q5. 객체 생성을 원천 차단하면 무조건 시스템 성능이 향상되나요?
> **답변 핵심**:  
> "그렇지 않습니다. JVM은 TLAB을 통한 빠른 메모리 할당과 JIT 컴파일러의 탈출 분석을 통해 객체의 스택 할당 및 스칼라 치환을 수행합니다. 불필요하게 가변 객체를 재사용하면 동시성 버그와 상태 오염 위험이 커지므로, 불변 객체를 통한 안전성을 확보한 뒤 프로파일링을 통해 실제 GC 병목이 입증된 구간만 최적화해야 합니다."

---

## 📋 오늘의 복습 체크리스트

- [ ] GC가 reachability를 기준으로 객체를 판단함을 설명할 수 있는가?
- [ ] GC Root의 3대 요소(스택 지역변수, Metaspace static 필드, JNI 참조)를 아는가?
- [ ] 약한 세대 가설과 Young/Old 분리 이유를 설명할 수 있는가?
- [ ] Eden, Survivor 0, Survivor 1의 역할과 Promotion 조건을 이해했는가?
- [ ] Stop-The-World가 P99 Tail Latency에 미치는 영향을 설명할 수 있는가?
- [ ] Throughput과 Latency의 차이 및 대표적인 GC(Parallel vs G1/ZGC)를 구분할 수 있는가?
- [ ] 객체 생성 조기 최적화의 위험성과 도메인 모델 불변성의 중요성을 설명할 수 있는가?
