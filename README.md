
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

# JPA의 getTargetConnection()까지

```
getTargetConnection:470, LazyConnectionDataSourceProxy$LazyConnectionInvocationHandler (org.springframework.jdbc.datasource)
invoke:443, LazyConnectionDataSourceProxy$LazyConnectionInvocationHandler (org.springframework.jdbc.datasource)
prepareStatement:-1, $Proxy90 (jdk.proxy3)
doPrepare:153, StatementPreparerImpl$5 (org.hibernate.engine.jdbc.internal)
prepareStatement:183, StatementPreparerImpl$StatementPreparationTemplate (org.hibernate.engine.jdbc.internal)
prepareQueryStatement:155, StatementPreparerImpl (org.hibernate.engine.jdbc.internal)
lambda$list$0:85, JdbcSelectExecutor (org.hibernate.sql.exec.spi)
apply:-1, JdbcSelectExecutor$$Lambda$1668/0x0000000801617698 (org.hibernate.sql.exec.spi)
executeQuery:231, DeferredResultSetAccess (org.hibernate.sql.results.jdbc.internal)
getResultSet:167, DeferredResultSetAccess (org.hibernate.sql.results.jdbc.internal)
advanceNext:218, JdbcValuesResultSetImpl (org.hibernate.sql.results.jdbc.internal)
processNext:98, JdbcValuesResultSetImpl (org.hibernate.sql.results.jdbc.internal)
next:19, AbstractJdbcValues (org.hibernate.sql.results.jdbc.internal)
next:66, RowProcessingStateStandardImpl (org.hibernate.sql.results.internal)
consume:188, ListResultsConsumer (org.hibernate.sql.results.spi)
consume:33, ListResultsConsumer (org.hibernate.sql.results.spi)
doExecuteQuery:209, JdbcSelectExecutorStandardImpl (org.hibernate.sql.exec.internal)
executeQuery:83, JdbcSelectExecutorStandardImpl (org.hibernate.sql.exec.internal)
list:76, JdbcSelectExecutor (org.hibernate.sql.exec.spi)
list:65, JdbcSelectExecutor (org.hibernate.sql.exec.spi)
load:145, SingleIdLoadPlan (org.hibernate.loader.ast.internal)
load:117, SingleIdLoadPlan (org.hibernate.loader.ast.internal)
load:75, SingleIdEntityLoaderStandardImpl (org.hibernate.loader.ast.internal)
doLoad:3715, AbstractEntityPersister (org.hibernate.persister.entity)
load:3704, AbstractEntityPersister (org.hibernate.persister.entity)
loadFromDatasource:604, DefaultLoadEventListener (org.hibernate.event.internal)
loadFromCacheOrDatasource:590, DefaultLoadEventListener (org.hibernate.event.internal)
load:560, DefaultLoadEventListener (org.hibernate.event.internal)
doLoad:544, DefaultLoadEventListener (org.hibernate.event.internal)
load:207, DefaultLoadEventListener (org.hibernate.event.internal)
proxyOrLoad:246, DefaultLoadEventListener (org.hibernate.event.internal)
doOnLoad:111, DefaultLoadEventListener (org.hibernate.event.internal)
onLoad:68, DefaultLoadEventListener (org.hibernate.event.internal)
applyEventToListener:-1, SessionImpl$$Lambda$1666/0x0000000801611d68 (org.hibernate.internal)
fireEventOnEachListener:138, EventListenerGroupImpl (org.hibernate.event.service.internal)
fireLoadNoChecks:1242, SessionImpl (org.hibernate.internal)
fireLoad:1230, SessionImpl (org.hibernate.internal)
load:209, IdentifierLoadAccessImpl (org.hibernate.loader.internal)
doLoad:160, IdentifierLoadAccessImpl (org.hibernate.loader.internal)
lambda$load$1:149, IdentifierLoadAccessImpl (org.hibernate.loader.internal)
get:-1, IdentifierLoadAccessImpl$$Lambda$1665/0x0000000801611040 (org.hibernate.loader.internal)
perform:112, IdentifierLoadAccessImpl (org.hibernate.loader.internal)
load:149, IdentifierLoadAccessImpl (org.hibernate.loader.internal)
find:2444, SessionImpl (org.hibernate.internal)
find:2415, SessionImpl (org.hibernate.internal)
invoke0:-1, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:77, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:43, DelegatingMethodAccessorImpl (jdk.internal.reflect)
invoke:568, Method (java.lang.reflect)
invoke:319, SharedEntityManagerCreator$SharedEntityManagerInvocationHandler (org.springframework.orm.jpa)
find:-1, $Proxy108 (jdk.proxy3)
findById:313, SimpleJpaRepository (org.springframework.data.jpa.repository.support)
invoke0:-1, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:77, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:43, DelegatingMethodAccessorImpl (jdk.internal.reflect)
invoke:568, Method (java.lang.reflect)
invokeJoinpointUsingReflection:351, AopUtils (org.springframework.aop.support)
lambda$new$0:277, RepositoryMethodInvoker$RepositoryFragmentMethodInvoker (org.springframework.data.repository.core.support)
invoke:-1, RepositoryMethodInvoker$RepositoryFragmentMethodInvoker$$Lambda$1653/0x00000008015dcd28 (org.springframework.data.repository.core.support)
doInvoke:170, RepositoryMethodInvoker (org.springframework.data.repository.core.support)
invoke:158, RepositoryMethodInvoker (org.springframework.data.repository.core.support)
invoke:516, RepositoryComposition$RepositoryFragments (org.springframework.data.repository.core.support)
invoke:285, RepositoryComposition (org.springframework.data.repository.core.support)
invoke:628, RepositoryFactorySupport$ImplementationMethodExecutionInterceptor (org.springframework.data.repository.core.support)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
doInvoke:168, QueryExecutorMethodInterceptor (org.springframework.data.repository.core.support)
invoke:143, QueryExecutorMethodInterceptor (org.springframework.data.repository.core.support)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
proceedWithInvocation:123, TransactionInterceptor$1 (org.springframework.transaction.interceptor)
invokeWithinTransaction:385, TransactionAspectSupport (org.springframework.transaction.interceptor)
invoke:119, TransactionInterceptor (org.springframework.transaction.interceptor)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
invoke:137, PersistenceExceptionTranslationInterceptor (org.springframework.dao.support)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
invoke:164, CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor (org.springframework.data.jpa.repository.support)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
invoke:97, ExposeInvocationInterceptor (org.springframework.aop.interceptor)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
invoke:95, MethodInvocationValidator (org.springframework.data.repository.core.support)
proceed:184, ReflectiveMethodInvocation (org.springframework.aop.framework)
invoke:220, JdkDynamicAopProxy (org.springframework.aop.framework)
findById:-1, $Proxy111 (jdk.proxy3)
findByIdDefault:18, MemberService (com.example.datasourceroutingtest.service)
invoke0:-1, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:77, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:43, DelegatingMethodAccessorImpl (jdk.internal.reflect)
invoke:568, Method (java.lang.reflect)
invokeJoinpointUsingReflection:351, AopUtils (org.springframework.aop.support)
intercept:713, CglibAopProxy$DynamicAdvisedInterceptor (org.springframework.aop.framework)
findByIdDefault:-1, MemberService$$SpringCGLIB$$0 (com.example.datasourceroutingtest.service)
findById:21, MemberServiceTest (com.example.datasourceroutingtest.service)
invoke0:-1, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:77, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:43, DelegatingMethodAccessorImpl (jdk.internal.reflect)
invoke:568, Method (java.lang.reflect)
invokeMethod:728, ReflectionUtils (org.junit.platform.commons.util)
proceed:60, MethodInvocation (org.junit.jupiter.engine.execution)
proceed:131, InvocationInterceptorChain$ValidatingInvocation (org.junit.jupiter.engine.execution)
intercept:156, TimeoutExtension (org.junit.jupiter.engine.extension)
interceptTestableMethod:147, TimeoutExtension (org.junit.jupiter.engine.extension)
interceptTestMethod:86, TimeoutExtension (org.junit.jupiter.engine.extension)
apply:-1, TestMethodTestDescriptor$$Lambda$194/0x0000000800d16678 (org.junit.jupiter.engine.descriptor)
lambda$ofVoidMethod$0:103, InterceptingExecutableInvoker$ReflectiveInterceptorCall (org.junit.jupiter.engine.execution)
apply:-1, InterceptingExecutableInvoker$ReflectiveInterceptorCall$$Lambda$195/0x0000000800d16a98 (org.junit.jupiter.engine.execution)
lambda$invoke$0:93, InterceptingExecutableInvoker (org.junit.jupiter.engine.execution)
apply:-1, InterceptingExecutableInvoker$$Lambda$1592/0x0000000801545e18 (org.junit.jupiter.engine.execution)
proceed:106, InvocationInterceptorChain$InterceptedInvocation (org.junit.jupiter.engine.execution)
proceed:64, InvocationInterceptorChain (org.junit.jupiter.engine.execution)
chainAndInvoke:45, InvocationInterceptorChain (org.junit.jupiter.engine.execution)
invoke:37, InvocationInterceptorChain (org.junit.jupiter.engine.execution)
invoke:92, InterceptingExecutableInvoker (org.junit.jupiter.engine.execution)
invoke:86, InterceptingExecutableInvoker (org.junit.jupiter.engine.execution)
lambda$invokeTestMethod$7:218, TestMethodTestDescriptor (org.junit.jupiter.engine.descriptor)
execute:-1, TestMethodTestDescriptor$$Lambda$1633/0x00000008015d8458 (org.junit.jupiter.engine.descriptor)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
invokeTestMethod:214, TestMethodTestDescriptor (org.junit.jupiter.engine.descriptor)
execute:139, TestMethodTestDescriptor (org.junit.jupiter.engine.descriptor)
execute:69, TestMethodTestDescriptor (org.junit.jupiter.engine.descriptor)
lambda$executeRecursively$6:151, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:-1, NodeTestTask$$Lambda$290/0x0000000800d322f0 (org.junit.platform.engine.support.hierarchical)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$8:141, NodeTestTask (org.junit.platform.engine.support.hierarchical)
invoke:-1, NodeTestTask$$Lambda$289/0x0000000800d320c8 (org.junit.platform.engine.support.hierarchical)
around:137, Node (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$9:139, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:-1, NodeTestTask$$Lambda$288/0x0000000800d31ca0 (org.junit.platform.engine.support.hierarchical)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
executeRecursively:138, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:95, NodeTestTask (org.junit.platform.engine.support.hierarchical)
accept:-1, SameThreadHierarchicalTestExecutorService$$Lambda$294/0x0000000800d32e08 (org.junit.platform.engine.support.hierarchical)
forEach:1511, ArrayList (java.util)
invokeAll:41, SameThreadHierarchicalTestExecutorService (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$6:155, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:-1, NodeTestTask$$Lambda$290/0x0000000800d322f0 (org.junit.platform.engine.support.hierarchical)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$8:141, NodeTestTask (org.junit.platform.engine.support.hierarchical)
invoke:-1, NodeTestTask$$Lambda$289/0x0000000800d320c8 (org.junit.platform.engine.support.hierarchical)
around:137, Node (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$9:139, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:-1, NodeTestTask$$Lambda$288/0x0000000800d31ca0 (org.junit.platform.engine.support.hierarchical)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
executeRecursively:138, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:95, NodeTestTask (org.junit.platform.engine.support.hierarchical)
accept:-1, SameThreadHierarchicalTestExecutorService$$Lambda$294/0x0000000800d32e08 (org.junit.platform.engine.support.hierarchical)
forEach:1511, ArrayList (java.util)
invokeAll:41, SameThreadHierarchicalTestExecutorService (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$6:155, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:-1, NodeTestTask$$Lambda$290/0x0000000800d322f0 (org.junit.platform.engine.support.hierarchical)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$8:141, NodeTestTask (org.junit.platform.engine.support.hierarchical)
invoke:-1, NodeTestTask$$Lambda$289/0x0000000800d320c8 (org.junit.platform.engine.support.hierarchical)
around:137, Node (org.junit.platform.engine.support.hierarchical)
lambda$executeRecursively$9:139, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:-1, NodeTestTask$$Lambda$288/0x0000000800d31ca0 (org.junit.platform.engine.support.hierarchical)
execute:73, ThrowableCollector (org.junit.platform.engine.support.hierarchical)
executeRecursively:138, NodeTestTask (org.junit.platform.engine.support.hierarchical)
execute:95, NodeTestTask (org.junit.platform.engine.support.hierarchical)
submit:35, SameThreadHierarchicalTestExecutorService (org.junit.platform.engine.support.hierarchical)
execute:57, HierarchicalTestExecutor (org.junit.platform.engine.support.hierarchical)
execute:54, HierarchicalTestEngine (org.junit.platform.engine.support.hierarchical)
execute:107, EngineExecutionOrchestrator (org.junit.platform.launcher.core)
execute:88, EngineExecutionOrchestrator (org.junit.platform.launcher.core)
lambda$execute$0:54, EngineExecutionOrchestrator (org.junit.platform.launcher.core)
accept:-1, EngineExecutionOrchestrator$$Lambda$244/0x0000000800d1cc00 (org.junit.platform.launcher.core)
withInterceptedStreams:67, EngineExecutionOrchestrator (org.junit.platform.launcher.core)
execute:52, EngineExecutionOrchestrator (org.junit.platform.launcher.core)
execute:114, DefaultLauncher (org.junit.platform.launcher.core)
execute:86, DefaultLauncher (org.junit.platform.launcher.core)
execute:86, DefaultLauncherSession$DelegatingLauncher (org.junit.platform.launcher.core)
processAllTestClasses:119, JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor (org.gradle.api.internal.tasks.testing.junitplatform)
access$000:94, JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor (org.gradle.api.internal.tasks.testing.junitplatform)
stop:89, JUnitPlatformTestClassProcessor (org.gradle.api.internal.tasks.testing.junitplatform)
stop:62, SuiteTestClassProcessor (org.gradle.api.internal.tasks.testing)
invoke0:-1, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:77, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:43, DelegatingMethodAccessorImpl (jdk.internal.reflect)
invoke:568, Method (java.lang.reflect)
dispatch:36, ReflectionDispatch (org.gradle.internal.dispatch)
dispatch:24, ReflectionDispatch (org.gradle.internal.dispatch)
dispatch:33, ContextClassLoaderDispatch (org.gradle.internal.dispatch)
invoke:94, ProxyDispatchAdapter$DispatchingInvocationHandler (org.gradle.internal.dispatch)
stop:-1, $Proxy2 (jdk.proxy1)
run:193, TestWorker$3 (org.gradle.api.internal.tasks.testing.worker)
executeAndMaintainThreadName:129, TestWorker (org.gradle.api.internal.tasks.testing.worker)
execute:100, TestWorker (org.gradle.api.internal.tasks.testing.worker)
execute:60, TestWorker (org.gradle.api.internal.tasks.testing.worker)
execute:56, ActionExecutionWorker (org.gradle.process.internal.worker.child)
call:113, SystemApplicationClassLoaderWorker (org.gradle.process.internal.worker.child)
call:65, SystemApplicationClassLoaderWorker (org.gradle.process.internal.worker.child)
run:69, GradleWorkerMain (worker.org.gradle.process.internal.worker)
main:74, GradleWorkerMain (worker.org.gradle.process.internal.worker)
```
