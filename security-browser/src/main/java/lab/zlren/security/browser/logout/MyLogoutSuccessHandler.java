package lab.zlren.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.security.core.support.SimpleResponse;
import lab.zlren.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功拦截器
 *
 * @author zlren
 * @date 17/10/23
 */
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 由于我们的这个组件不是以Component来配的，所以不能注入其他组件
     * 通过构造函数将它set进去
     */
    private SecurityProperties securityProperties;

    public MyLogoutSuccessHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * 处理登出成功拦截器
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
        log.info("退出成功");

        // 自定义退出成功处理逻辑
        // 配了页面就跳转，否则以json的形式返回
        String signOutUrl = securityProperties.getBrowser().getSignOutUrl();
        if (StringUtils.isBlank(signOutUrl)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
        } else {
            httpServletResponse.sendRedirect(signOutUrl);
        }
    }
}
