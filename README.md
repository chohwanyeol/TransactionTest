---

# 🔁 트랜잭션 전파 및 롤백 실험 프로젝트

Spring Boot 기반으로 트랜잭션 전파 방식과 예외 처리 방식에 따라 DB 반영 결과가 어떻게 달라지는지를 실험한 프로젝트입니다.  
트랜잭션은 예외가 발생하면 자동으로 롤백될 것이라 생각하기 쉽지만, 실제로는 **예외 종류, 전파 전략, try-catch 여부**에 따라 전혀 다른 결과가 나타납니다.

---

## ✅ 프로젝트 정보

- **프로젝트명**: Transaction Propagation Test
- **기술스택**: Java 21, Spring Boot 3.2.5, Spring Web, Lombok, Spring Data JPA, H2 Database, Swagger
- **실험 목적**:
  - `@Transactional`의 전파 옵션(`REQUIRES_NEW`, `REQUIRED`)에 따른 트랜잭션 흐름 차이 실험
  - Checked vs Unchecked Exception 발생 시 롤백 동작 확인
  - `rollbackFor` 설정 유무에 따른 결과 비교
  - 실무에서 자주 발생하는 “왜 롤백 안 되지?” 상황의 원리 직접 체험

---

## 📦 주요 의존성

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-devtools'
implementation 'org.projectlombok:lombok'
runtimeOnly 'com.h2database:h2'
```

---

## 🔧 실행 방법

```bash
# 프로젝트 실행
./gradlew bootRun
```

## 🧪 실험 API

| Method | Endpoint     | 설명 |
|--------|--------------|------|
| GET    | `/test`      | 부모 → 자식 호출 상황에서 트랜잭션 전파 및 롤백 동작 실험 |

---

## 🧬 실험 케이스 요약

| 케이스 | 설정 | 결과 |
|--------|-------|-------|
| 1 | 자식: `REQUIRES_NEW`, 부모가 예외 try-catch | ✅ 부모 커밋, ❌ 자식 롤백 |
| 2 | 자식: `REQUIRED`, 부모가 예외 try-catch | ❌ 둘 다 롤백 (rollback-only 상태) |
| 3 | 자식: `REQUIRES_NEW` + `throw new Exception()` | ✅ 둘 다 커밋 (자식 롤백 안 됨) |
| 4 | 위와 같지만 `rollbackFor = Exception.class` 추가 | ✅ 부모 커밋, ❌ 자식 롤백 |
| 5 | 부모에서 `@Transactional` 제거 | ✅ 부모 커밋, ❌ 자식 롤백 |

---

## 🧠 느낀 점: "롤백은 자동이 아니다. 예외 처리 방식이 중요."

- 처음엔 “예외가 나면 트랜잭션은 자동으로 롤백되겠지”라고 생각했지만,  
  실험 결과는 전혀 달랐다.
  
- 핵심 배운 점:
  - Spring은 기본적으로 **RuntimeException만 롤백**한다.
  - **Checked Exception은 rollbackFor로 명시해야 롤백됨**
  - `REQUIRES_NEW`는 별도 트랜잭션이지만, 부모가 예외를 catch하지 않으면 전체 롤백됨
  - 트랜잭션 흐름은 단순히 어노테이션 하나로 끝나는 게 아니라, **예외 처리 방식과 호출 흐름까지 설계해야 제대로 동작**함

- 실무에서 만나는 “왜 데이터가 커밋됐지?” 같은 상황을 **실제로 재현하고 원인을 이해**할 수 있었던 좋은 경험이었다.

---
