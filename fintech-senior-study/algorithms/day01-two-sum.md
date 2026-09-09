# Day 1 알고리즘 — Two Sum (두 수의 합) 완전 분석

> **LeetCode 1번 / 코딩테스트 기초 필수 문제**  
> "정수 배열 `numbers`와 정수 `target`이 주어졌을 때, 두 수의 합이 `target`이 되는 두 원소의 인덱스를 찾아라." (각 입력은 정확히 하나의 해가 존재하며, 동일한 요소를 중복 사용할 수 없음)

---

## 📌 1. 문제 예시

```java
int[] numbers = {2, 7, 11, 15};
int target = 9;
```

* `numbers[0] + numbers[1] = 2 + 7 = 9`
* 반환값: `[0, 1]`

---

## 🐢 2. 접근법 1: 완전 탐색 (Brute Force) — $O(N^2)$

가장 직관적인 방법은 모든 가능한 두 수의 쌍을 2중 반복문으로 대조하는 것입니다.

```java
public static int[] findTwoSumBruteForce(int[] numbers, int target) {
    for (int firstIndex = 0; firstIndex < numbers.length; firstIndex++) {
        for (int secondIndex = firstIndex + 1; secondIndex < numbers.length; secondIndex++) {
            if (numbers[firstIndex] + numbers[secondIndex] == target) {
                return new int[]{firstIndex, secondIndex};
            }
        }
    }
    throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
}
```

### 💡 한 줄씩 짚어보는 핵심
* `secondIndex = firstIndex + 1`부터 시작하는 이유:
  1. 자기 자신과 더하는 것을 방지 (`firstIndex == secondIndex` 방지).
  2. 이미 검사한 쌍(예: `2 + 7`을 검사한 후 나중에 `7 + 2`)의 중복 연산을 제거.
* **복잡도 분석**:
  * **시간복잡도**: $O(N^2)$ — 데이터가 10배 늘어나면 연산 횟수는 100배 증가.
  * **공간복잡도**: $O(1)$ — 추가 메모리 사용 없음.

---

## ⚡ 3. 접근법 2: 역발상과 HashMap 1-Pass 최적화 — $O(N)$

### 3.1. 핵심 아이디어: "필요한 숫자를 역으로 계산한다"
현재 탐색 중인 숫자가 `7`이고 `target`이 `9`라면, 필요한 숫자는 오직 하나입니다:
$$\text{requiredNumber} = \text{target} - \text{currentNumber} = 9 - 7 = 2$$

> **"내가 필요한 숫자 2를 이전에 본 적이 있는가?"**

매번 배열을 다시 뒤지지 않고, **"이전에 본 숫자와 그 인덱스"**를 빠른 메모장인 `HashMap`에 적어두면 $O(1)$에 찾을 수 있습니다.

```text
key   = 방문한 숫자 (number)
value = 그 숫자의 인덱스 (index)
```

### 3.2. HashMap 최적화 코드
```java
public static int[] findTwoSumIndices(int[] numbers, int target) {
    Map<Integer, Integer> indexByNumber = new HashMap<>();

    for (int currentIndex = 0; currentIndex < numbers.length; currentIndex++) {
        int currentNumber = numbers[currentIndex];
        int requiredNumber = target - currentNumber;

        Integer requiredIndex = indexByNumber.get(requiredNumber);
        if (requiredIndex != null) {
            return new int[]{requiredIndex, currentIndex};
        }

        indexByNumber.put(currentNumber, currentIndex);
    }

    throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
}
```

---

## 🔍 4. 손추적 (Dry-run) 실행 흐름 다이어그램

입력: `numbers = [2, 7, 11, 15]`, `target = 9`

```text
[Loop 1] currentIndex = 0, currentNumber = 2
         requiredNumber = 9 - 2 = 7
         Map에 7이 있는가? ➔ 없음!
         Map에 현재 값 기록: { 2: 0 }

[Loop 2] currentIndex = 1, currentNumber = 7
         requiredNumber = 9 - 7 = 2
         Map에 2가 있는가? ➔ 있음! (인덱스: 0)
         정답 즉시 반환: [0, 1] ➔ 알고리즘 종료!
```

---

## 🚨 5. 가장 중요한 실수 포인트: "왜 조회를 먼저 하고 저장을 나중에 하는가?"

다음 코너 케이스를 생각해봅니다:
```java
int[] numbers = {3, 3};
int target = 6;
```

만약 **"저장"을 먼저 하고 "조회"를 나중에** 한다면 어떻게 될까요?
1. 첫 번째 `3`을 만나자마자 Map에 `{ 3: 0 }`을 넣음.
2. `required = 6 - 3 = 3`을 조회함.
3. 방금 내가 넣은 `0`번 인덱스가 조회됨!
4. 결과: `[0, 0]` ➔ **동일한 원소 하나를 두 번 사용하는 치명적 오답 발생!**

따라서 반드시:
> **1단계: 필요한 짝(`requiredNumber`)을 Map에서 먼저 찾는다.**  
> **2단계: 없을 때만 현재 숫자(`currentNumber`)를 Map에 저장한다.**

이 순서를 지켜야 자기 자신을 중복 참조하지 않고, `numbers = [3, 3]`일 때도 두 번째 `3` 시점에 첫 번째 `3`을 정확히 매칭할 수 있습니다.

---

## 💎 6. 6년 차 백엔드 엔지니어의 Clean Modern Java 리팩터링

### 6.1. 원시 배열 `int[]` 대신 불변 Record 객체 활용
`int[] {0, 1}`은 0과 1이 무엇을 의미하는지 도메인 의미가 드러나지 않습니다. Java 17+ `record`를 사용하여 의미를 명확히 캡슐화합니다.

```java
public record IndexPair(int firstIndex, int secondIndex) {
    public IndexPair {
        if (firstIndex == secondIndex) {
            throw new IllegalArgumentException("두 인덱스는 동일할 수 없습니다.");
        }
    }
}
```

### 6.2. HashMap 초기 용량(Initial Capacity) 최적화
Java `HashMap`의 기본 용량은 16, load factor는 0.75입니다. 데이터가 12개를 넘어가면 2배로 확장(Rehashing)되면서 버킷 재할당 오버헤드가 발생합니다. 배열 크기 $N$을 미리 안다면 리사이징을 방지할 수 있습니다.

$$\text{initialCapacity} = \left\lceil \frac{N}{0.75} \right\rceil + 1$$

```java
int initialCapacity = (int) Math.ceil(numbers.length / 0.75f) + 1;
Map<Integer, Integer> indexByNumber = new HashMap<>(initialCapacity);
```

### 6.3. 왜 Stream API를 쓰지 않는가?
Two Sum은 루프를 돌며 이전 요소들을 Map에 누적해가는 **상태(Stateful)가 있는 알고리즘**입니다. 이를 무리하게 `IntStream`이나 Stream reduce로 구현하면 가독성이 현저히 떨어지고 디버깅이 어려워집니다.
> **Effective Java 원칙**: "스트림을 과용하면 프로그램이 읽거나 유지보수하기 어려워진다."

---

## 🎤 7. 면접 30초 실전 답변 스크립트

> **면접관**: "Two Sum 문제를 어떻게 해결하셨나요?"
>
> **답변 스크립트**:  
> "가장 단순한 접근은 2중 for문으로 모든 쌍을 대조하는 브루트포스 방식으로 시간복잡도는 $O(N^2)$입니다. 이를 개선하기 위해 배열을 단 한 번만 순회하는 $O(N)$ 1-Pass HashMap 풀이를 적용했습니다.  
> 현재 숫자를 $x$라고 할 때 필요한 보수(Complement)는 `target - x`입니다. 지금까지 방문한 숫자와 인덱스를 HashMap에 기록해두면, 필요한 보수가 존재하는지 평균 $O(1)$의 상수 시간에 확인할 수 있습니다.  
> 특히 동일한 요소를 중복 사용하는 오류를 방지하기 위해, 현재 값을 맵에 저장하기 전에 보수의 존재 여부를 먼저 조회하는 순서로 구현했습니다. 추가 공간복잡도는 맵에 최대 $N$개를 저장하므로 $O(N)$이며, 시간과 공간의 전형적인 트레이드오프를 활용한 최적화입니다."

---

## 📝 8. 복습 연습 문제 3종 (손으로 직접 추적해보기)

1. **문제 A**: `numbers = [3, 2, 4]`, `target = 6` ➔ 정답: `[1, 2]`
   * Loop 1 (`3`): Map `{}` ➔ 필요한 값 `3` 없음 ➔ `{3: 0}` 저장
   * Loop 2 (`2`): 필요한 값 `4` 없음 ➔ `{3: 0, 2: 1}` 저장
   * Loop 3 (`4`): 필요한 값 `2` 조회 성공 (`index 1`) ➔ `[1, 2]` 반환
2. **문제 B**: `numbers = [3, 3]`, `target = 6` ➔ 왜 `[0, 0]`이 아니라 `[0, 1]`이 나오는지 설명해보기.
3. **문제 C**: `numbers = [1, 5, 8, 3, 9]`, `target = 12` ➔ 직접 종이에 Map의 변화 과정을 적고 정답 도출하기.
