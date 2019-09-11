package top.agno.gnosis.repeatcommit.repeatcommit;

import org.aspectj.lang.ProceedingJoinPoint;

public interface CacheKeyGenerator {

    /**
     * 获取AOP参数,生成指定缓存Key
     *
     * @param pjp  PJP
     * @param lock
     * @return 缓存KEY
     */
    String getLockKey(ProceedingJoinPoint pjp, CacheLock lock);
}
