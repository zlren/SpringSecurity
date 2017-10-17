package lab.zlren.security.browser.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.security.core.properties.LoginType;
import lab.zlren.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义验证成功之后的逻辑
 * 理论上该去实现这个接口 implements AuthenticationSuccessHandler
 * 我们继承的这个类是ss对上面接口的实现，错误那里也是同理
 *
 * @author zlren
 * @date 17/10/15
 */
@Component
@Slf4j
public class MyAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 这个方法在验证成功之后会被调用
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication      封装认证信息，根据登录方式的不通内容会有所区别
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, Authentication authentication) throws IOException, ServletException {

        log.info("登陆成功");

        if (LoginType.JSON.equals(this.securityProperties.getBrowser().getLoginType())) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(this.objectMapper.writeValueAsString(authentication));
        } else {
            // 父类的方法就是跳转
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }
    }
}
