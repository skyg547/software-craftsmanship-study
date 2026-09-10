# Day 3 알고리즘 — 투 포인터 (Two Pointer) 패턴 정복

> **핵심 개념**: 배열이나 문자열에서 두 개의 위치 인덱스(포인터)를 양 끝 또는 특정 위치에 두고 조건에 따라 이동시키며 탐색 범위를 좁혀가는 최적화 기법.  
> **시간복잡도**: $O(N)$, **추가 공간복잡도**: $O(1)$

---

## 🎯 1. Two Pointer의 직관적 사고방식

```text
[1, 2, 4, 6, 10], target = 10

  1    2    4    6    10
  ↑                    ↑
left                 right
```

* 양쪽 끝에 손가락을 하나씩 올립니다.
* **현재 합이 target보다 작다면?** ➔ 더 큰 숫자가 필요하므로 `left`를 오른쪽으로 한 칸 이동 (`left++`)
* **현재 합이 target보다 크다면?** ➔ 더 작은 숫자가 필요하므로 `right`를 왼쪽으로 한 칸 이동 (`right--`)
* **언제까지?** ➔ 두 포인터가 서로 교차할 때까지 (`left < right`)

---

## 💡 2. 문제 1 — 정렬된 배열에서 두 수의 합 (Sorted Two Sum)

### 2.1. 탐색 시뮬레이션
배열: `[1, 2, 4, 6, 10]`, `target = 10`

1. `left=0(1), right=4(10)` ➔ `sum = 1 + 10 = 11 > 10` (큼) ➔ `right--`
2. `left=0(1), right=3(6)`  ➔ `sum = 1 + 6 = 7 < 10` (작음) ➔ `left++`
3. `left=1(2), right=3(6)`  ➔ `sum = 2 + 6 = 8 < 10` (작음) ➔ `left++`
4. `left=2(4), right=3(6)`  ➔ `sum = 4 + 6 = 10 == 10` (일치!) ➔ **`[2, 3]` 즉시 반환**

### 2.2. Clean Java 구현
```java
public static int[] findPairIndices(int[] sortedNumbers, int target) {
    int leftIndex = 0;
    int rightIndex = sortedNumbers.length - 1;

    while (leftIndex < rightIndex) {
        int currentSum = sortedNumbers[leftIndex] + sortedNumbers[rightIndex];

        if (currentSum == target) {
            return new int[]{leftIndex, rightIndex};
        }

        if (currentSum < target) {
            leftIndex++;
            continue;
        }

        rightIndex--;
    }

    throw new IllegalArgumentException("조건을 만족하는 두 수의 쌍이 존재하지 않습니다.");
}
```

---

## ⚖️ 3. HashMap vs Two Pointer: 상황별 최적 선택 기준

| 비교 항목 | Day 1: HashMap 풀이 | Day 3: Two Pointer 풀이 |
| :--- | :--- | :--- |
| **전제 조건** | **정렬되지 않은 임의의 배열** | **이미 오름차순 정렬된 배열** |
| **시간복잡도** | 평균 $O(N)$ | $O(N)$ |
| **추가 공간** | $O(N)$ (Map 메모리 필요) | **$O(1)$** (변수 2개만 필요) |
| **적합 상황** | 원본 인덱스를 그대로 찾아야 하고 정렬 비용을 들이기 싫을 때 | 입력이 정렬되어 있고 추가 메모리 할당을 극도로 줄여야 할 때 |

### ⚠️ 시니어 면접 함정 질문
> **면접관**: "정렬되지 않은 일반 Two Sum에도 정렬 후 Two Pointer를 쓰면 더 좋지 않나요?"
>
> **시니어의 답변**:  
> "그렇지 않습니다. 정렬되지 않은 배열을 Two Pointer로 풀려면 먼저 정렬을 수행해야 하므로 **$O(N \log N)$의 정렬 시간 비용**이 발생합니다. 또한 정렬 과정에서 원본 인덱스가 뒤섞이므로 인덱스를 보존하기 위한 추가 객체 할당이 수반됩니다. 따라서 원본 인덱스 검색이 목적이라면 추가 공간 $O(N)$을 사용하더라도 전체 시간복잡도를 $O(N)$으로 방어하는 **HashMap 단일 패스 풀이**가 더 우수합니다."

---

## 🔄 4. 문제 2 — 회문 (Palindrome) 검사

문자열의 앞뒤가 대칭인지 확인할 때도 Two Pointer가 가장 우아합니다.

```text
"level"
 l  e  v  e  l
 ↑           ↑
 L           R  ➔ 'l' == 'l' (일치, L++, R--)

 l  e  v  e  l
    ↑     ↑
    L     R     ➔ 'e' == 'e' (일치, L++, R--)

 L == R ➔ 회문 검사 완료 (true)
```

```java
public static boolean isPalindrome(String value) {
    int leftIndex = 0;
    int rightIndex = value.length() - 1;

    while (leftIndex < rightIndex) {
        if (value.charAt(leftIndex) != value.charAt(rightIndex)) {
            return false;
        }
        leftIndex++;
        rightIndex--;
    }

    return true;
}
```

---

## 🧩 5. 3대 필수 자료구조/알고리즘 패턴 맵핑

```text
1. "과거에 방문했던 값/인덱스를 기억해야 하는가?"
   ➔ HashMap (평균 O(1) 조회)

2. "오직 값의 존재 여부(중복 방지, 멱등성)만 필요한가?"
   ➔ HashSet (O(1) 검사/추가)

3. "데이터가 정렬되어 있거나 양 끝에서 범위를 좁혀갈 수 있는가?"
   ➔ Two Pointer (O(N) 시간, O(1) 메모리)
```
