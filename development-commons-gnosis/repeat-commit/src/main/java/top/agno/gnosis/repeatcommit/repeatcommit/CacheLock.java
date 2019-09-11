package top.agno.gnosis.repeatcommit.repeatcommit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * redis 锁key的前缀
     *
     * @return redis 锁key的前缀
     */
    String prefix() default "";

    /**
     * 过期秒数,默认为5秒
     *
     * @return 轮询锁的时间
     */
    int expire() default 5;

    /**
     * 超时时间单位
     *
     * @return 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * <p>Key的分隔符（默认 :）</p>
     * <p>生成的Key：N:SO1008:500</p>
     *
     * @return String
     */
    String delimiter() default ":";

    /**
     * <p>是否全局用户控制重复提交</p>
     * 默认控制全局用户
     * 解释：true，则注解的接口只能有一个用户在同一时间访问
     * false，则注解的接口可以有多用户在同一时间访问
     */
    boolean globaluser() default true;
}