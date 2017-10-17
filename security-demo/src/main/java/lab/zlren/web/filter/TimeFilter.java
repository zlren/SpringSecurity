package lab.zlren.web.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author zlren
 * @date 17/10/12
 */
@Slf4j
// @Component // 拦截所有
public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        log.info("time filter start");

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse); // 这句话是去执行此次http请求对应的controller方法
        long endTime = System.currentTimeMillis();

        log.info("cost: {}", endTime - startTime);

        log.info("time filter finish");
    }

    @Override
    public void destroy() {
        log.info("time filter destroy");
    }
}
