package lab.zlren.security.app.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
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

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

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

        String header = httpServletRequest.getHeader("Authorization");

        if (header == null || !header.startsWith("Basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        String[] tokens = this.extractAndDecodeHeader(header, httpServletRequest);

        assert tokens.length == 2;

        String clientId = tokens[0];
        String clientSecret = tokens[1];

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在: " + clientId);
        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配: " + clientId);
        }

        // 创建tokenrequest的第一个参数是根据不同的请求类型来的，然后会根据类型去构建Authentication
        // 但是成功处理器里面已经有了Authentication，所以第一个参数传个空的map就好了
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(this.objectMapper.writeValueAsString(token));

    }

    /**
     * 解码拿到用户名和密码，放在一个String数组中返回
     *
     * @param header
     * @param request
     * @return
     * @throws IOException
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
        byte[] base64Token = header.substring(6).getBytes("UTF-8");

        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}
