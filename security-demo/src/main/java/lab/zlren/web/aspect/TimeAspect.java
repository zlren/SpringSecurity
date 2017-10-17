package lab.zlren.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author zlren
 * @date 17/10/12
 */
@Aspect
// @Component
@Slf4j
public class TimeAspect {

    /**
     * 这个参数包括了被拦截的Controller方法的具体信息
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* lab.zlren.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("time aspect start");

        // 方法参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            log.info("arg is {}", arg);
        }

        long startTime = System.currentTimeMillis();
        // 去执行被拦截的方法，然后拿到返回值
        Object o = proceedingJoinPoint.proceed();
        log.info("time aspect end：{}", System.currentTimeMillis() - startTime);

        return o;
    }
}
