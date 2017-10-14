package lab.zlren.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by zlren on 17/10/12.
 */
@Slf4j
@Component
public class TimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o) throws Exception {

        log.info("preHandle");
        log.info("类名 {}", ((HandlerMethod) o).getBean().getClass().getName());
        log.info("方法名 {}", ((HandlerMethod) o).getMethod().getName());
        httpServletRequest.setAttribute("startTime", new Date().getTime());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

        log.info("postHandle");
        log.info("耗时：{}", new Date().getTime() - (long) httpServletRequest.getAttribute("startTime"));
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

        log.info("afterCompletion");
        log.info("postHandle");
        log.info("耗时：{}", new Date().getTime() - (long) httpServletRequest.getAttribute("startTime"));

        if (e != null) {
            log.info("异常是:{}", e.getMessage());
        } else {
            log.info("没有异常");
        }
    }
}
