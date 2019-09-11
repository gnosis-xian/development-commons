package top.agno.gnosis.repeatcommit.repeatcommit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class LockMethodInterceptor {

    @Autowired
    public LockMethodInterceptor(StringRedisTemplate lockRedisTemplate, CacheKeyGenerator cacheKeyGenerator) {
        this.lockRedisTemplate = lockRedisTemplate;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    private final StringRedisTemplate lockRedisTemplate;
    private final CacheKeyGenerator cacheKeyGenerator;


    @Around("execution(public * *(..)) && @annotation(cn.net.yto.fms.commons.utils.repeatcommit.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("Redis prefix未定义");
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjp, lock);
        try {
            String value = lockRedisTemplate.opsForValue().get(lockKey);
            if (value == null) {
                lockRedisTemplate.opsForValue().set(lockKey, "", (long) lock.expire(), lock.timeUnit());
            } else {
                throw new RuntimeException("操作速度过快，请稍后再试");
            }
            return pjp.proceed();
        } finally {
            lockRedisTemplate.delete(lockKey);
        }
    }
}