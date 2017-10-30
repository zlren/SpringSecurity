package lab.zlren.security.app.controller;

import lab.zlren.security.app.social.AppSignUpUtils;
import lab.zlren.security.core.support.SocialUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zlren
 * @date 17/10/25
 */
@RestController
public class AppSecurityController {

    @Autowired
    private AppSignUpUtils appSignUpUtils;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @GetMapping("/social/signup")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SocialUserInfo getSocialUserInfo(HttpServletRequest httpServletRequest) {

        // 这一次从session中拿出来，保存起来，下一次再过来session中就没有了
        Connection<?> connectionFromSession = providerSignInUtils.getConnectionFromSession(new ServletWebRequest
                (httpServletRequest));
        appSignUpUtils.saveConnectionData(new ServletWebRequest(httpServletRequest),
                connectionFromSession.createData());

        return new SocialUserInfo()
                .setProviderId(connectionFromSession.getKey().getProviderId())
                .setProviderUserId(connectionFromSession.getKey().getProviderUserId())
                .setNickname(connectionFromSession.getDisplayName())
                .setHeadimg(connectionFromSession.getImageUrl());
    }
}
