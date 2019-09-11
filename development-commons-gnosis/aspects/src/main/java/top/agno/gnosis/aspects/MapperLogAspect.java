package top.agno.gnosis.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Aspect
@Configuration
@Slf4j
public class MapperLogAspect {

    @Autowired
    private Environment environment;

    @Before("execution(* *.*.*.*.mapper.*Mapper.*(..))")
    public void before(JoinPoint pjp) throws Throwable {
        String profilesActive = this.environment.getProperty("spring.profiles.active");
        String logReqResp = this.environment.getProperty("system.log.sql.params");

        // 如果是生产环境则不打印请求日志
        if (IsProcessUtil.isProdEnv(profilesActive)) {
            return;
        }
        // 如果设置打印请求为false
        if (IsProcessUtil.isRequire(logReqResp)) {
            return;
        }

        String sqlParam = "SqlParams sqlId:{}, sql params:{}";
        log.info(sqlParam, pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()));
    }
}