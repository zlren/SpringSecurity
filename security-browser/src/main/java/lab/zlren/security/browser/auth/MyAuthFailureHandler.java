package lab.zlren.security.browser.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.security.browser.support.SimpleResponse;
import lab.zlren.security.core.properties.LoginType;
import lab.zlren.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证失败以后的处理逻辑
 * Created by zlren on 17/10/15.
 */
@Component
@Slf4j
// implements AuthenticationFailureHandler
public class MyAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e                   包含了认证过程中发生的错误所产生的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        log.info("登陆失败");

        if (LoginType.JSON.equals(this.securityProperties.getBrowser().getLoginType())) {
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter()
                    .write(this.objectMapper.writeValueAsString(new SimpleResponse(e.getMessage()))); // 只返回错误消息
        } else {
            // 父类默认跳转
            super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        }
    }
}
