package top.agno.gnosis.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.agno.gnosis.utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author: gaojing [01381583@yto.net.cn]
 */
@Aspect
@Configuration
@Slf4j
public class LogRecordAspect {

    @Autowired
    private Environment environment;

    @Pointcut("execution(* *.*.*.*.controller.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        String profilesActive = this.environment.getProperty("spring.profiles.active");
        String logReqResp = this.environment.getProperty("system.log.request.response");

        // 如果是生产环境则不打印请求日志
        if (IsProcessUtil.isProdEnv(profilesActive)) {
            return pjp.proceed();
        }
        // 如果设置打印请求为false
        if (IsProcessUtil.isRequire(logReqResp)) {
            return pjp.proceed();
        }

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        Object[] args = pjp.getArgs();

        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String param = request.getQueryString();
        String params = Arrays.toString(args);
        String userAgent = request.getHeader("user-agent");
        String ip = request.getRemoteAddr();
        log.info(String.format(" === [Request], %s, %s, %s, %s, %s, %s, %s", method, url, uri, params, param, ip, userAgent));

        Object result = pjp.proceed();

        log.info(String.format(" === [Response], %s, %s, %s", method, url, JsonUtil.toString(result)));

        return result;
    }
}