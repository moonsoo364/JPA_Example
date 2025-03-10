### 1차 캐시에서 조회

그림과 같이 `em.find()`를 호출하면 1차 캐시에서 식별자 값으로 엔티티를 찾는다.  
만약 엔티티가 있으면 데이터베이스를 조회하지 않고 메모리의 1차 캐시에서 엔티티를 조회한다.

**유용한 케이스:**  
SSE(Server Sent Event)를 이용하여 다수의 클라이언트에게 데이터를 조회해서 전달해야할 때
한 트랜잭션에서 같은 엔티티를 여러 번 조회하는 경우 1차 캐시를 거치면 빠르게 조회할 수 있다.

#### **엔티티를 저장할 때**
```java
Member member = new Member();
member.setId("member1");
member.setUsername("회원1");

em.persist(member);

Member findMember = em.find(Member.class, "member1"); // 1차 캐시에서 조회
```

#### **엔티티를 조회할 때**
```java
memberRepository.findById("member1"); // DB에서 조회
memberRepository.findById("member1"); // 1차 캐시에서 조회
```

---

### repository의 `save()` 메서드 내부 코드

1. `persist()`는 새로운 엔티티를 영속성 컨텍스트에 추가한다.
   - 이때 새로운 엔티티는 `SELECT` 문 없이 바로 반환된다.
2. `merge()`는 비영속 상태의 엔티티를 영속성 컨텍스트에 병합한다.
   - 1차 캐시에 엔티티가 있는 경우 `SELECT`로 가져온 후 병합
   - 1차 캐시에 이미 있다면 `SELECT` 없이 병합 후 반환

```java
@Transactional
@Override
public <S extends T> S save(S entity) {
    if (entityInformation.isNew(entity)) {
        em.persist(entity);  // 새로운 엔티티 -> persist() 호출
        return entity;
    } else {
        return em.merge(entity);  // 기존 엔티티 -> merge() 호출
    }
}
```

---

### `@Transactional`에서 `persist()` 후 필드 변경 시 DB에 저장되는 값

#### **실행 코드**
```java
@Transactional
public void testPersist() {
    User user = new User("john_doe", "john@example.com");
    User savedUser = userRepository.save(user);  // persist() 호출
    System.out.println(savedUser.getId());  // 1차 캐시에서 가져옴

    savedUser.setEmail("kail@example.com");  // 이메일 변경
}
```

---

## **1. 실행 흐름 분석**

### **1) `save(user)` 호출 → `persist()` 실행**
- `userRepository.save(user)` 호출 → 내부적으로 `persist(user)` 수행
- `user` 객체는 **1차 캐시에 저장**되고, **영속 상태**가 됨
- 아직 DB에 `INSERT` 쿼리가 실행되지 않음 (트랜잭션 커밋 시점에 실행됨)

---

### **2) `savedUser.setEmail("kail@example.com")` 실행**
- `savedUser`는 **영속 상태 (persistent state)**
- **영속 상태의 엔티티는 필드 값이 변경될 경우 자동으로 감지 (Dirty Checking)**
- 따라서 `setEmail("kail@example.com")`을 호출하면 **JPA가 자동으로 `UPDATE`를 수행**

---

### **3) 트랜잭션이 종료될 때 (`@Transactional`)**
- 트랜잭션이 끝날 때 (`commit` 시점) JPA는 변경 사항을 DB에 반영
- **SQL 실행 순서**
    1. `INSERT` 실행 (`flush()` 시점)
    2. `UPDATE` 실행 (Dirty Checking 감지)

---

## **2. 실행되는 SQL**
```sql
INSERT INTO users (username, email) VALUES ('john_doe', 'john@example.com');

UPDATE users SET email = 'kail@example.com' WHERE id = 1;
```
- 먼저 `INSERT` 실행 → `john@example.com` 저장
- 이후 Dirty Checking을 통해 `UPDATE` 실행 → `email`이 `kail@example.com`으로 변경

---

## **3. 결론**
- `save(user)`는 `persist(user)`를 호출하므로 엔티티는 **영속 상태**
- **Dirty Checking(변경 감지)**이 동작하여 **email 값이 변경되면 자동으로 `UPDATE` 실행**
- 최종적으로 DB에 저장되는 값:
```sql
username = 'john_doe'
email = 'kail@example.com'  -- 변경된 값이 반영됨
```

---

### **Dirty Checking(변경 감지)을 비활성화하는 방법**

JPA의 Dirty Checking 기능을 끄고 싶다면 아래 방법을 사용할 수 있다.

---

## **1. `flush()` 후 `detach()` 사용**
`EntityManager.detach(entity)`를 사용하면 엔티티를 **준영속(detached) 상태**로 만들어 Dirty Checking이 발생하지 않도록 할 수 있다.

#### **코드 예제**
```java
@Transactional
public void testDetach() {
    User user = new User("john_doe", "john@example.com");
    userRepository.save(user);  // persist() 호출

    entityManager.flush();  // 즉시 INSERT 실행
    entityManager.detach(user);  // 영속성 컨텍스트에서 제거

    user.setEmail("kail@example.com");  // Dirty Checking 안 됨
}
```

#### **실행되는 SQL**
```sql
INSERT INTO users (username, email) VALUES ('john_doe', 'john@example.com');
```
- `flush()`를 호출하여 DB에 **즉시 INSERT 실행**
- `detach(user)`를 호출하여 **영속성 컨텍스트에서 제거**
- 이후 `setEmail("kail@example.com")`을 실행해도 **Dirty Checking이 발생하지 않음**

---

## **2. `clear()` 사용하여 영속성 컨텍스트 초기화**
`entityManager.clear()`를 사용하면 **모든 엔티티를 영속성 컨텍스트에서 제거**하여 Dirty Checking을 방지할 수 있다.

#### **코드 예제**
```java
@Transactional
public void testClear() {
    User user = new User("john_doe", "john@example.com");
    userRepository.save(user);

    entityManager.flush();
    entityManager.clear();  // 영속성 컨텍스트 초기화

    user.setEmail("kail@example.com");  // Dirty Checking 안 됨
}
```

- `clear()`를 호출하면 **모든 엔티티가 준영속 상태로 변경**되므로 이후 변경 사항이 DB에 반영되지 않음.

---

## **3. `saveAndFlush()` 사용 후 read-only 변경**
Spring Data JPA의 `saveAndFlush()`를 사용하면 **즉시 DB에 반영**하고, 이후 `read-only` 설정을 변경하여 Dirty Checking을 방지할 수도 있다.

#### **코드 예제**
```java
@Transactional
public void testSaveAndFlush() {
    User user = new User("john_doe", "john@example.com");
    userRepository.saveAndFlush(user);  // 즉시 INSERT 실행

    entityManager.setProperty("org.hibernate.readOnly", true);  // 변경 감지 방지

    user.setEmail("kail@example.com");  // Dirty Checking 안 됨
}
```

---

## **4. `@Transactional(readOnly = true)` 사용**
Spring의 `@Transactional(readOnly = true)`를 사용하면 **쓰기 작업이 금지**되어 Dirty Checking이 발생하지 않는다.

#### **코드 예제**
```java
@Transactional(readOnly = true)
public void testReadOnly() {
    User user = userRepository.findById(1L).orElseThrow();
    user.setEmail("kail@example.com");  // Dirty Checking이 발생하지 않음
}
```
- `readOnly = true` 설정을 하면 **`persist()`는 동작하지 않고 조회만 가능**
- 변경 사항을 감지하지 않으므로 **UPDATE 쿼리가 실행되지 않음**

---

### **Dirty Checking 방지 방법 정리**
| 방법 | 설명 | 적용 대상 |
|------|------|------|
| `detach(entity)` | 특정 엔티티를 영속성 컨텍스트에서 제거 | 특정 엔티티 |
| `clear()` | 모든 엔티티를 영속성 컨텍스트에서 제거 | 전체 컨텍스트 |
| `saveAndFlush()` 후 read-only 설정 | 즉시 반영 후 변경 감지 방지 | 특정 엔티티 |
| `@Transactional(readOnly = true)` | 트랜잭션 내에서 변경 감지 비활성화 | 전체 메서드 |
