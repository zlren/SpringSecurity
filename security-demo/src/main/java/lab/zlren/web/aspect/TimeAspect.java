package lab.zlren.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Date;

/**
 * Created by zlren on 17/10/12.
 */
@Aspect
// @Component
@Slf4j
public class TimeAspect {

    @Around("execution(* lab.zlren.web.controller.UserController.*(..))")
    // 这个参数包括了被拦截的Controller方法的具体信息
    public Object handleControllerMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { //

        log.info("time aspect start");

        Object[] args = proceedingJoinPoint.getArgs(); // 方法参数
        for (Object arg : args) {
            log.info("arg is {}", arg);
        }

        long startTime = new Date().getTime();
        Object o = proceedingJoinPoint.proceed(); // 去执行被拦截的方法，然后拿到返回值
        log.info("time aspect end：{}", new Date().getTime() - startTime);

        return o;
    }
}
