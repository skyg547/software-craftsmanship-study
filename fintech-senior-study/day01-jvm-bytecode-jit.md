# Day 1 — Java 코드가 실행되기까지: JDK · JVM · Bytecode · JIT

> **학습 목표**: 내가 작성한 Java 소스 코드가 어떻게 컴파일되고, ClassLoader를 거쳐 JVM 메모리에 올라가며, JIT 컴파일러에 의해 기계어로 최적화되어 실행되는지 전체 파이프라인을 6년 차 시니어 관점에서 완벽히 설명할 수 있다.

---

## 🧭 1. 전체 실행 흐름도 (10분 조감도)

우리가 작성한 코드는 CPU가 직접 이해할 수 없으므로 다단계 변환 및 최적화 과정을 거칩니다.

```text
       [ 개발자 작성 ]
       AccountService.java
              │
              │ javac (Java Compiler)
              ▼
       [ 가상 기계어 ]
       AccountService.class (Bytecode)
              │
              ▼
┌─────────────────────────────────────────────────────────────┐
│                           JVM                               │
│                                                             │
│  [Class Loader 서브시스템]                                   │
│  Loading ➔ Linking (Verify, Prepare, Resolve) ➔ Init        │
│              │                                              │
│              ▼                                              │
│  [JVM Runtime Data Areas]                                   │
│  Metaspace (클래스 메타데이터) / Heap (객체) / Stack (프레임)     │
│              │                                              │
│              ▼                                              │
│  [Execution Engine]                                         │
│  ┌──────────────────────────────────────────────┐           │
│  │ 1. Interpreter (초기 바이트코드 한 줄씩 해석)     │           │
│  │ 2. Profiler (호출 빈도 관찰 ➔ Hotspot 감지)   │           │
│  │ 3. JIT Compiler (자주 호출되는 코드 기계어 컴파일) │           │
│  │    • C1 (Client): 빠른 컴파일, 기본 최적화     │           │
│  │    • C2 (Server): 고도화 최적화 (인라이닝, 탈출분석)│          │
│  │ 4. Garbage Collector                         │           │
│  └──────────────────────┬───────────────────────┘           │
└─────────────────────────┼───────────────────────────────────┘
                          │ Native Machine Code
                          ▼
                     [ Host CPU ]
                  (x86-64 / ARM64)
```

---

## 🔍 2. 전문 설명 & 초보자 직관 해석 (2단 구조)

### 2.1. JVM (Java Virtual Machine)
* **전문 설명**: Java Bytecode 명세를 준수하여 클래스 파일을 로딩하고, 메모리를 관리(GC)하며, 플랫폼 독립적인 바이트코드를 특정 OS/CPU 아키텍처의 네이티브 코드로 변환해 실행하는 가상 실행 환경. OpenJDK의 대표적인 구현체는 **HotSpot VM**입니다.
* **초보자 직관 해석**:
  > **JVM은 '동시 통역사 + 호텔 지배인 + 보안요원'입니다.**  
  > 내가 쓴 Java 코드를 CPU가 알아듣는 기계어로 실시간 통역해주고, 객체들이 머물 방(메모리)을 배정하고 청소(GC)해주며, 위험한 메모리 침범을 막아줍니다.

### 2.2. Bytecode (`.class`)
* **전문 설명**: `javac` 컴파일러가 생성하는 이진 코드 형식(Binary Format)으로, JVM의 스택 기반 명령어 셋(Instruction Set: `iload`, `invokevirtual`, `ireturn` 등)으로 구성된 플랫폼 중립적 중간 표현식.
* **초보자 직관 해석**:
  > **Bytecode는 '국제 공용 에스페란토어 악보'입니다.**  
  > 윈도우든 리눅스든 Mac이든 상관없이 모든 연주자(각 OS용 JVM)가 읽고 각자의 악기(CPU)로 연주할 수 있는 중간 악보입니다.

### 2.3. JIT (Just-In-Time) 컴파일러
* **전문 설명**: 인터프리터의 느린 실행 속도를 보완하기 위해 런타임 프로파일링을 기반으로 빈번히 실행되는 코드(Hot Code)를 감지하여 런타임 중에 네이티브 기계어로 직접 컴파일하고 코드 캐시(Code Cache)에 보관하여 재사용하는 동적 컴파일러.
* **초보자 직관 해석**:
  > **JIT은 '자주 묻는 질문을 통째로 외워버리는 베테랑 통역사'입니다.**  
  > 처음 1~2번은 단어장(인터프리터)을 뒤적이며 번역하지만, 은행 송금 루프처럼 수만 번 반복되는 질문은 아예 한국어-영어를 한 덩어리로 외워서(기계어 컴파일) 질문이 나오자마자 0.001초 만에 즉답합니다.

---

## ⚙️ 3. 심층 파이프라인 1: ClassLoader 3단계 동작 원리

클래스 파일이 JVM 메모리에 올라가는 과정은 엄격한 3단계를 거칩니다.

1. **로딩 (Loading)**:
   * `.class` 바이너리 데이터를 읽어와 JVM **Metaspace**(Java 8 이전 PermGen)에 클래스 메타데이터(`java.lang.Class` 인스턴스)를 생성합니다.
   * 계층 구조: *Bootstrap ClassLoader ➔ Platform(Extension) ClassLoader ➔ Application(System) ClassLoader*
2. **링킹 (Linking)**:
   * **Verification (검증)**: 바이트코드가 JVM 명세를 지키는지, 스택 오버플로우나 유효하지 않은 메모리 접근이 없는지 보안 검증 (가장 시간 소모 큼).
   * **Preparation (준비)**: 클래스의 정적 필드(`static` 변수)를 위한 메모리를 할당하고 기본값(`0`, `null`, `false`)으로 초기화.
   * **Resolution (해석)**: 런타임 상수 풀의 심볼릭 참조(클래스/메서드 이름 문자열)를 실제 메모리 주소(직접 참조)로 변환.
3. **초기화 (Initialization)**:
   * `static` 초기화 블록(`static { ... }`)과 정적 필드의 실제 정의된 초기화 값을 할당.

---

## 🚀 4. 심층 파이프라인 2: HotSpot JIT 컴파일러와 Tiered Compilation

HotSpot JVM은 프로그램을 시작하자마자 모든 코드를 JIT 컴파일하지 않습니다. 컴파일 자체도 CPU 비용이 크기 때문입니다.

### 4.1. 계층형 컴파일 (Tiered Compilation) 5단계
Java 7부터 도입되어 Java 8+의 기본값이 된 메커니즘입니다:

* **Level 0 (Interpreter)**: 순수 인터프리터로 실행하며 호출 횟수와 루프 백엣지(Backedge) 카운터를 수집.
* **Level 1 (Simple C1)**: 프로파일링 데이터 수집 없이 C1(Client Compiler)으로 빠르게 컴파일하여 기본 기계어 실행.
* **Level 2 (Limited C1)**: 기본적인 프로파일링(메서드 호출, 루프 카운터)만 포함하여 C1 컴파일.
* **Level 3 (Full C1)**: 분기 확률, 타입 프로파일링까지 포함하여 C1 풀 컴파일.
* **Level 4 (C2 Server Compiler)**: 축적된 프로파일링 데이터를 기반으로 **공격적인 최적화(Aggressive Optimization)**를 적용하여 극한의 네이티브 코드 생성.

### 4.2. C2 컴파일러의 5대 핵심 최적화 기법
1. **메서드 인라이닝 (Method Inlining)**:
   * 작은 메서드(예: Getter/Setter, 도메인 유효성 검사)의 바이트코드를 호출부 코드 안으로 직접 붙여넣어 함수 호출 오버헤드(Stack Frame 생성/소멸) 제거.
2. **탈출 분석 (Escape Analysis)**:
   * 객체가 메서드 밖으로 탈출(Escape)하지 않는지 분석.
   * 탈출하지 않는다면 **힙(Heap)에 객체를 할당하지 않고 스택에 할당**하거나, 객체를 분해하여 기본형 변수로 대체(**스칼라 치환, Scalar Replacement**) ➔ GC 발생 자체를 원천 차단!
3. **루프 언롤링 (Loop Unrolling)**:
   * 루프의 반복 횟수를 줄여 분기 예측 실패 비용과 인덱스 증가 연산을 축소.
4. **OSR (On-Stack Replacement)**:
   * 메서드가 종료되지 않은 채로 긴 루프를 도는 도중에도, 인터프리터 스택 프레임을 즉시 최적화된 기계어 스택 프레임으로 교체하여 실행 가속.
5. **역최적화 (Deoptimization)**:
   * JIT의 가정이 깨지면(예: 다형성 인터페이스에 새로운 클래스가 런타임에 유입되는 경우) 안전하게 인터프리터 모드로 롤백.

---

## 🏦 5. 금융 시스템 실무 트레이드오프: JVM Warm-up과 Cold Start

### 5.1. 금융 현업의 문제: 장 시작 09:00 첫 결제/주문 지연 (Latency Spike)
* 배포 직후 새로 뜬 Pod는 아직 **Interpreter 단계**에 머물러 있습니다.
* 장 시작 시점에 대량 트래픽이 유입되면, 인터프리터 실행 + JIT C1/C2 컴파일러의 CPU 점유율 스파이크가 겹쳐 수초 간의 **레이턴시 튐(Tail Latency Spike)**이 발생합니다.

### 5.2. 실무 아키텍처 대책
1. **트래픽 유입 전 사전 웜업 (Warm-up Traffic)**:
   * 쿠버네티스 Readiness Probe 통과 전에 더미 트랜잭션을 수천 번 호출하여 핵심 비즈니스 메서드(이자 계산, 원장 검증)를 C2 레벨까지 미리 컴파일 유도.
2. **Class Data Sharing (CDS / AppCDS)**:
   * 클래스 메타데이터를 미리 덤프해두어 기동 시 로딩 및 검증 시간 40~50% 단축.
3. **GraalVM Native Image (AOT 컴파일) 트레이드오프 검토**:
   * *장점*: 기동 시간 수십 ms, 메모리 footprint 극소화, Cold Start 제거.
   * *단점*: C2의 런타임 프로파일링 기반 극한 최적화(PGO 없이는)보다 피크 처리량(Peak Throughput)이 다소 낮을 수 있으며 리플렉션 설정 복잡.

---

## 🎤 6. 면접 핵심 질문 & 답변 가이드

### Q1. JVM에서 인터프리터와 JIT 컴파일러의 공존 이유와 Tiered Compilation을 설명해주세요.
> **답변 핵심**:  
> "인터프리터는 기동 즉시 코드를 실행할 수 있지만 반복 실행 시 느리고, JIT 컴파일러는 최고 성능의 네이티브 코드를 내지만 컴파일 자체에 CPU 비용이 듭니다. Java는 이 둘의 장점을 결합하여 초반에는 인터프리터로 빠르게 기동하고 프로파일링 정보를 수집한 뒤, 호출 빈도가 높은 핫코드에 대해 C1으로 1차 가속, C2로 인라이닝 및 탈출 분석을 적용한 고도화 최적화를 적용하는 Tiered Compilation 방식을 채택합니다."

### Q2. 탈출 분석(Escape Analysis)이 GC에 어떤 긍정적 영향을 미치나요?
> **답변 핵심**:  
> "메서드 내부에서 생성된 객체가 메서드 외부로 참조되지 않는다는 것이 증명되면, JVM은 이를 힙에 할당하지 않고 스택에 할당하거나 스칼라 치환을 통해 기본형 변수로 분해합니다. 이를 통해 객체 생성에 따른 힙 할당 비용과 GC 압력을 획기적으로 줄여 시스템 전반의 응답 지연을 방어합니다."

---

## 📌 오늘의 핵심 암기 5포인트

1. `Java ➔ javac ➔ Bytecode (.class) ➔ ClassLoader ➔ JVM ➔ JIT ➔ CPU Machine Code`
2. ClassLoader는 `Loading ➔ Linking (Verify/Prepare/Resolve) ➔ Initialization`을 거친다.
3. JIT은 전체 컴파일이 아니라 **Hotspot 코드만 동적 최적화**한다.
4. Tiered Compilation은 C1의 빠른 기동과 C2의 극한 최적화를 조화시킨다.
5. 탈출 분석은 스택 할당과 스칼라 치환을 통해 GC 오버헤드를 원천 차단한다.
