
# Transaction Annotation이 없을 때 기본으로 readonly=true

아래와 같이 @Transactional 붙지 않은 Service 메서드를 실행했을 때 MyBatis 환경에서는 default로 readonly가 꺼져있었지만,JPA가 사용되면 readonly가 켜져있다.

```java
fun findByIdDefault(id: Int) = memberRepository.findById(id)
```

트랜잭션 어노테이션의 정보를 파싱하기 위해
1. 처음 시도는 Repository의 메서드 레벨을 분석하고,
2. 두 번째로는 Repository 인터페이스 레벨을 확인하며,
3. 세 번째로는 구현체 클래스의 메서드를 분석하고,
4. 네 번째로는 구현체 클래스를 분석한다.

> 위의 트랜잭션 정보가 한 번 찾아지면 `AbstractFallbackTransactionAttributeSource.attributeCache` 필드에 캐시된다.

```
attributeCache = {ConcurrentHashMap@13151}  size = 1
    {MethodClassKey@13172} "public abstract java.util.Optional com.example.datasourceroutingtest.repository.MemberRepository.findById(int) on class org.springframework.data.jpa.repository.support.SimpleJpaRepository" -> {RuleBasedTransactionAttribute@13158} "PROPAGATION_REQUIRED,ISOLATION_DEFAULT,readOnly"
        key = {MethodClassKey@13172} "public abstract java.util.Optional com.example.datasourceroutingtest.repository.MemberRepository.findById(int) on class org.springframework.data.jpa.repository.support.SimpleJpaRepository"
        value = {RuleBasedTransactionAttribute@13158} "PROPAGATION_REQUIRED,ISOLATION_DEFAULT,readOnly"
```

```java
@Override
@Nullable
protected TransactionAttribute computeTransactionAttribute(Method method, @Nullable Class<?> targetClass) {
    // ...

    // First try is the method in the target class.
    txAtt = findTransactionAttribute(specificMethod);

    if (txAtt != null) {
      return txAtt;
    }

    // Second try is the transaction attribute on the target class.
    txAtt = findTransactionAttribute(specificMethod.getDeclaringClass());

    // 세 번째는 구현체 클래스의 메서드를 분석한다.
    // public java.util.Optional org.springframework.data.jpa.repository.support.SimpleJpaRepository.findById(java.lang.Object)

    // 네 번째는 구현체 클래스 레벨에서 분석한다.
}
```

네 번째에서 분석한 SimpleJpaRepository의 클래스 래벨에 어노테이션이 작성되어 있다. 그래서 default로 readonly가 켜져있는 것
```java
@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {
    ...
}
```

처음부터 SimpleJpaRepository를 확인했으면 저 정도로 분석안해도 됐는데 생각을 못 했다.  

