package lab.zlren.security.core.auth.mobile;

import lab.zlren.security.core.properties.SecurityConstant;
import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.validate.code.ValidateCodeException;
import lab.zlren.security.core.validate.code.sms.SmsCode;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 继承OncePerRequestFilter，这是Spring提供的一个工具，保证只调用1次
 * 图片验证码校验拦截器，这里实际上拦截了所有的请求，但是只对特定的需要校验的请求进行校验，在if那里提现
 *
 * @author zlren
 * @date 17/10/15
 */
public class SmsCodeFilter extends OncePerRequestFilter { // implements InitializingBean 实现这个接口的目的就是初始化urls

    @Setter
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 需要被校验的url
     */
    private Set<String> urls = new HashSet<>();

    @Setter
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 这个方法是InitializingBean接口的方法
     * 但实际上OncePerRequestFilter已经实现了这个方法，这里覆盖一下
     *
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        // 这里面配置的路径有带*号的那种通配符的形式
        String url = securityProperties.getCode().getSms().getUrl();
        if (url != null) {
            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(url, ",");
            urls.addAll(Arrays.asList(configUrls));
        }

        // 登录页面提交数据是一定要校验的
        urls.add("/auth/mobile");
    }

    /**
     * 这里对图形验证码进行校验
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 只有在访问登录页面提交表单的时候，才会进行拦截校验
        // 这里改进为支持可配置路径，因为也许并不只有登录页面提交数据的url需要校验验证码，也许其他的地方还需要
        boolean isNeed = false;
        for (String url : urls) {
            // 注意匹配url通配符需要这种工具
            if (antPathMatcher.match(url, httpServletRequest.getRequestURI())) {
                isNeed = true;
            }
        }

        if (isNeed) {

            try {
                validate(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                // 在校验验证码的过程中遇到异常，就没必要执行过滤器链下面的过滤器了
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    /**
     * 验证码校验
     *
     * @param servletWebRequest
     */
    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {

        SmsCode smsCodeInSession = (SmsCode) sessionStrategy.getAttribute(servletWebRequest, SecurityConstant
                .SESSION_KEY_SMS_CODE);

        // 从用户提交的请求中拿到用户的输入
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),
                "smsCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (smsCodeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (smsCodeInSession.isExpired()) {
            sessionStrategy.removeAttribute(servletWebRequest, SecurityConstant.SESSION_KEY_SMS_CODE);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(smsCodeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(servletWebRequest, SecurityConstant.SESSION_KEY_SMS_CODE);
    }
}
