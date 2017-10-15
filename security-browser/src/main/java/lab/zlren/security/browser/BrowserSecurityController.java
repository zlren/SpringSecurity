package lab.zlren.security.browser;

import lab.zlren.security.browser.support.SimpleResponse;
import lab.zlren.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zlren on 17/10/15.
 */
@RestController
@Slf4j
public class BrowserSecurityController {

    // 需要跳转是ss做的，它会跳到loginPage参数设置的地址，也就是下面这个controller方法，跳转过来的时候将相关的参数缓存到了这里
    // 从这里可以从缓存中将被拦截的请求拿出来
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 当需要身份认证时，跳转到这里
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/auth/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SimpleResponse authRequire(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse) throws IOException {

        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的地址是：{}", targetUrl);

            // 这是一个html请求
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                // 这里第三个参数就是登录的页面，如果做成可配置的，就不要把这里写死
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse,
                        this.securityProperties.getBrowser().getLoginPage());
            }
        }

        // 如果是数据请求，这里要返回json，这里简单包装一下SimpleResponse
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }

}
