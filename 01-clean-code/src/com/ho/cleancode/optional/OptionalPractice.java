package com.ho.cleancode.optional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OptionalPractice {

    public record User(Long id, String name, String email) {
        public static final User GUEST = new User(0L, "게스트", "guest@company.com");
    }

    private final Map<Long, User> userDatabase = new HashMap<>();

    public OptionalPractice() {
        userDatabase.put(1L, new User(1L, "하성호", "skyg547@gmail.com"));
        userDatabase.put(2L, new User(2L, "홍길동", "gildong@company.com"));
    }

    /**
     * 값이 없을 수도 있음을 Optional<User>로 명시
     */
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(userDatabase.get(id));
    }

    /**
     * [황금률] 컬렉션은 절대 null을 리턴하지 않고 빈 리스트를 리턴함
     */
    public List<String> getUserRoles(Long id) {
        if (!userDatabase.containsKey(id)) {
            return Collections.emptyList(); // null 대신 빈 컬렉션 반환
        }
        return List.of("ROLE_USER", "ROLE_ACCOUNTANT");
    }

    public void runDemo() {
        System.out.println("\n--- [Optional<T> 실습 데모] ---");

        // 1. orElseThrow: 사용자 존재 시 정상 처리, 없으면 예외
        User user1 = findUserById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: 1"));
        System.out.println("1. orElseThrow 성공: " + user1.name() + " (" + user1.email() + ")");

        // 2. orElse: 없으면 기본값(GUEST)으로 대체
        User unknownUser = findUserById(999L)
                .orElse(User.GUEST);
        System.out.println("2. orElse 기본값 처리: " + unknownUser.name() + " (" + unknownUser.email() + ")");

        // 3. ifPresent: 값이 있을 때만 안전하게 실행
        findUserById(2L).ifPresent(u -> System.out.println("3. ifPresent 실행: 환영합니다, " + u.name() + "님!"));

        // 4. 컬렉션 null-safe 처리
        List<String> roles = getUserRoles(999L);
        System.out.println("4. 빈 리스트 반환 (null 체크 불필요): " + roles.size() + "개 역할 (오류 없이 for문 실행 가능)");
    }
}
