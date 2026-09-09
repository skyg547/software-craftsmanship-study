# Day 2 — JVM 메모리 구조 & GC의 출발점: Stack · Heap · Reference

> **학습 목표**: 메서드 호출 시 생성되는 Stack Frame과 Heap에 할당되는 객체 인스턴스의 차이를 규명하고, "참조(Reference)"의 본질과 가비지 컬렉션(GC)의 도달 가능성(Reachability) 원리를 금융 도메인 실무 코드와 연계하여 완벽히 설명할 수 있다.

---

## 🧭 1. JVM 런타임 메모리 조감도 (초보자 2단 비유)

```text
┌─────────────────────────────────────────────────────────────┐
│                    JVM Runtime Data Areas                   │
│                                                             │
│  [ 각 Thread 전용 공간 (스레드 독립적) ]                       │
│  ┌────────────────────────┐  ┌────────────────────────┐     │
│  │     Thread 1 Stack     │  │     Thread 2 Stack     │     │
│  │ ┌────────────────────┐ │  │ ┌────────────────────┐ │     │
│  │ │ transfer() Frame   │ │  │ │ order() Frame      │ │     │
│  │ ├────────────────────┤ │  │ ├────────────────────┤ │     │
│  │ │ calculate() Frame  │ │  │ │ validate() Frame   │ │     │
│  │ └────────────────────┘ │  │ └────────────────────┘ │     │
│  └───────────┬────────────┘  └───────────┬────────────┘     │
│              │                           │                  │
│              └─────────────┬─────────────┘                  │
│                            │ 참조 (Reference)               │
│                            ▼                                │
│  [ 모든 Thread 공유 공간 ]                                    │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Heap Area (객체 인스턴스, 배열)                        │ │
│  │  • Young Generation (Eden ➔ S0 / S1)                   │ │
│  │  • Old Generation (Tenured)                            │ │
│  └────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Metaspace (Native Memory, Java 8+)                     │ │
│  │  • Class Metadata, Static 변수/메서드, 상수 풀           │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 💡 초보자 직관 2단 비유
* **Stack**: **"각 직원의 개인 작업 책상 (1인용 메모장)"**
  * 각 스레드마다 따로 배정되며, 다른 직원은 내 책상의 메모를 볼 수 없습니다.
  * 메서드가 시작되면 메모지(Stack Frame)를 한 장 꺼내어 작업하고, 메서드가 끝나면 메모지를 찢어서 휴지통에 버립니다.
* **Heap**: **"회사 전체 직원이 함께 쓰는 중앙 공용 창고"**
  * `new` 키워드로 생성된 모든 실제 물건(객체)들이 보관되는 곳입니다.
  * 여러 직원이 같은 물건의 주소(Reference)를 공유하여 동시에 건드릴 수 있습니다.

---

## 🧱 2. Thread Stack과 Stack Frame 구조

### 2.1. Stack Frame의 구성 요소
메서드가 호출될 때마다 Thread의 Stack에 하나의 **Stack Frame**이 `push`되고, 종료되면 `pop`됩니다.

1. **지역 변수 배열 (Local Variable Array)**:
   * 매개변수와 메서드 내부의 지역 변수를 인덱스(0부터)로 저장.
   * 인스턴스 메서드의 경우 `index 0`은 항상 현재 객체인 `this` 참조.
2. **피연산자 스택 (Operand Stack)**:
   * JVM 바이트코드 연산(더하기, 곱하기, 값 로드)이 일어나는 임시 계산 작업대.
3. **프레임 데이터 (Frame Data)**:
   * 메서드가 속한 클래스의 런타임 상수 풀 참조, 정상 반환 정보, 예외 디스패치 테이블 등.

### 2.2. StackOverflowError의 원인
* 스택의 깊이가 JVM에 할당된 스택 메모리 크기(`-Xss`, 기본 1MB)를 초과할 때 발생.
* 주로 탈출 조건이 잘못된 **깊은 재귀 호출**이나 상호 순환 호출 시 발생.

---

## 📦 3. Heap 메모리 구조와 객체 라이프사이클

### 3.1. Weak Generational Hypothesis (약한 세대 가설)
JVM 가비지 컬렉터의 핵심 설계 철학:
1. **대부분의 객체는 생성된 후 아주 짧은 시간 동안만 살아남는다.** (예: 메서드 내 임시 DTO, 계산용 객체)
2. **오래된 객체에서 젊은 객체로의 참조는 매우 적다.**

### 3.2. Heap의 구역 분할
* **Young Generation**:
  * **Eden**: `new`로 생성된 신규 객체가 최초로 배치되는 공간.
  * **Survivor 0 / Survivor 1**: Eden이 꽉 차서 **Minor GC**가 발생할 때, 여전히 살아남은 객체가 복사되어 이동하는 곳. (두 Survivor 중 하나는 항상 비어 있어야 함)
  * 객체가 Minor GC에서 살아남을 때마다 `age bit`가 1씩 증가.
* **Old Generation (Tenured)**:
  * `age`가 임계치(기본 MaxTenuringThreshold=15)를 넘긴 장수 객체들이 승격(Promotion)되어 보관되는 공간.
  * 이곳이 가득 차면 전체 힙을 검사하는 **Major GC (Full GC)**가 발생하여 심각한 애플리케이션 멈춤(STW) 유발.

---

## ⚠️ 4. Primitive vs Reference: 금융 코드의 치명적 함정

### 4.1. "참조 복사"와 잔액 오염 버그
```java
Account first = new Account(100_000L);
Account second = first; // ⚠️ 객체가 복사된 것이 아니라 '주소'만 복사됨!

second.withdraw(30_000L);

System.out.println(first.getBalance());  // 70,000원! (first도 함께 출금됨)
```

```text
[ Stack ]                     [ Heap ]
first  ─────────┐
                ├─── 주소 0x100 ───▶ [ Account 객체 (balance: 70,000) ]
second ─────────┘
```

* **원인**: `first`와 `second`는 독립된 두 객체가 아니라 **힙 메모리의 동일한 인스턴스를 가리키는 두 개의 참조 포인터**에 불과합니다.
* **금융 실무 위험**: 결제 처리 중 원본 엔티티 객체를 조회하여 캐시나 다른 스레드에 그대로 넘겨 수정을 가할 경우, 원본 계좌의 잔액이나 상태가 예기치 않게 오염되는 동시성 버그가 발생합니다.

### 4.2. 해결책: 불변 객체(Immutable Record)와 방어적 복사
```java
public record Money(long amount) {

    public Money {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 음수일 수 없습니다: " + amount);
        }
    }

    public Money subtract(Money other) {
        if (this.amount < other.amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        return new Money(this.amount - other.amount); // 항상 새로운 불변 객체 반환
    }

    public Money add(Money other) {
        return new Money(Math.addExact(this.amount, other.amount)); // 오버플로우 방어
    }
}
```

---

## 🧹 5. Reference와 가비지 컬렉션(GC) 메커니즘

### 5.1. 도달 가능성 (Reachability)과 GC Root
Java는 C++처럼 프로그래머가 직접 메모리를 해제하지 않으며, **도달 가능성(Reachability)** 알고리즘(Tracing GC)을 사용합니다.

* **GC Root (출발점)**:
  1. 현재 실행 중인 모든 스레드의 **Stack Frame 내 지역 변수 및 매개변수**
  2. Metaspace에 로드된 클래스의 **`static` 참조 변수**
  3. JNI(Java Native Interface)에 의해 생성된 C/C++ 네이티브 참조
* **판정 기준**:
  * GC Root로부터 참조 체인(Reference Chain)을 따라 도달할 수 있는 객체 = **Reachable (생존)**
  * 도달할 수 없는 고립된 객체 = **Unreachable (GC 수거 대상)**

```text
[ GC Root: Stack ]
      │
      ▼
   [ Node A ] ───▶ [ Node B ]    (Reachable: 생존!)

   [ Node C ] ◀──▶ [ Node D ]    (서로 순환 참조 중이지만 GC Root와 연결 끊김 ➔ Unreachable: GC 수거!)
```

### 5.2. Java에서도 Memory Leak이 발생하는 이유
> **"GC가 있는데 왜 메모리 누수가 발생하나요?"**

* 객체를 비즈니스 로직상으로는 더 이상 쓰지 않지만, **어딘가에 살아있는 GC Root가 해당 객체를 여전히 참조**하고 있다면 GC는 이를 살아있는 객체로 판단하여 영원히 수거하지 못합니다.
* **대표적인 실무 누수 원인 3가지**:
  1. `static Map<Key, Value>`과 같은 전역 캐시에 객체를 넣고 제거(`remove`)하지 않는 경우.
  2. 톰캣 등의 스레드 풀 환경에서 `ThreadLocal`을 사용한 후 `ThreadLocal.remove()`를 호출하지 않아 스레드가 반환되어도 데이터가 잔존하는 경우.
  3. 리스너나 콜백을 등록해두고 명시적으로 해제하지 않는 경우.

---

## 🎤 6. 시니어 백엔드 실전 면접 질문 5선

### Q1. Stack과 Heap의 차이점을 스레드 안전성(Thread Safety) 관점에서 설명해주세요.
> **답변 핵심**:  
> "Stack은 스레드마다 독립적으로 할당되는 스레드 고유(Thread-local) 메모리 영역이므로, Stack Frame 내의 기본형 지역 변수는 본질적으로 스레드 안전합니다. 반면 Heap은 프로세스 내 모든 스레드가 공유하는 영역이므로, 여러 스레드가 동시에 힙 상의 동일한 객체 참조에 접근하여 상태를 변경할 경우 동기화(Synchronization)나 불변 객체(Immutability) 설계가 없으면 동시성 데이터 불일치가 발생합니다."

### Q2. `Account second = first;`를 수행했을 때 메모리에서 일어나는 일을 설명해주세요.
> **답변 핵심**:  
> "새로운 `Account` 객체가 힙에 할당되는 것이 아니라, `first` 변수가 들고 있던 힙 메모리의 32비트 또는 64비트 참조 주소값(Reference value)이 `second` 변수의 스택 공간으로 복사됩니다. 결과적으로 두 변수는 동일한 힙 인스턴스를 가리키게 됩니다."

### Q3. `==` 연산자와 `equals()` 메서드의 본질적 차이는 무엇인가요?
> **답변 핵심**:  
> "`==` 연산자는 스택에 저장된 원시 값(Primitive)의 일치 여부 또는 객체의 참조 주소값(Identity)이 동일한지를 물리적으로 비교합니다. 반면 `equals()`는 객체의 논리적 동등성(Logical Equality)을 비교하기 위해 클래스 설계자가 재정의하는 메서드입니다. Effective Java 원칙에 따라 `equals`를 재정의할 때는 반드시 `hashCode`도 함께 일관되게 재정의해야 HashMap/HashSet 같은 해시 기반 컬렉션에서 정상 동작합니다."

### Q4. Java 가비지 컬렉터에서 Stop-The-World (STW)란 무엇이며, 왜 최소화해야 하나요?
> **답변 핵심**:  
> "GC를 실행하기 위해 JVM이 애플리케이션의 모든 사용자 스레드를 일시 중단시키는 현상입니다. STW가 길어지면 금융 결제나 주문 API의 P99 응답 지연(Tail Latency)이 수초 이상 튀어 타임아웃 장애로 이어질 수 있으므로, 세대 분리 수집, G1 GC나 ZGC 같은 저지연 컬렉터 도입, 불필요한 객체 할당 억제를 통해 STW 시간을 밀리초 단위로 억제해야 합니다."

### Q5. 참조가 끊어진 객체는 그 즉시 메모리에서 제거되나요?
> **답변 핵심**:  
> "아닙니다. 참조가 끊긴 객체는 단순히 '가비지 수거 대상(Eligible for GC)'으로 마킹될 뿐이며, 실제 물리적 메모리 회수는 JVM의 메모리 압박 정도와 GC 알고리즘 스케줄러의 판단에 따라 비동기적으로 수행됩니다."

---

## 📋 오늘의 복습 체크리스트

- [ ] Stack Frame의 3대 요소(지역변수 배열, 피연산자 스택, 프레임 데이터)를 설명할 수 있는가?
- [ ] Heap의 Young(Eden, S0, S1)과 Old Generation의 역할과 Minor/Major GC의 차이를 아는가?
- [ ] `Account second = first;`가 왜 객체 복사가 아닌지 메모리 그림을 그릴 수 있는가?
- [ ] 금융 시스템에서 불변 객체(Record)와 `Math.addExact`를 사용하는 이유를 설명할 수 있는가?
- [ ] GC Root로부터의 Reachability 판정 원리와 순환 참조 객체의 수거 방식을 이해했는가?
- [ ] Java에서 GC가 존재함에도 Memory Leak이 발생하는 3대 원인을 설명할 수 있는가?
