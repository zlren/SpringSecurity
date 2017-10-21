package lab.zlren.security.core.auth.mobile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 这个provider负责处理SmsCodeAuthToken，这一点在support方法中提现
 *
 * @author zlren
 * @date 17/10/20
 */
public class SmsCodeAuthProvider implements AuthenticationProvider {

    @Getter
    @Setter
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthToken smsCodeAuthToken = (SmsCodeAuthToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String) smsCodeAuthToken.getPrincipal());

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        // 两个信息的构造函数，在这个构造函数中有这样的一句话setAuthenticated(true)
        SmsCodeAuthToken authResult = new SmsCodeAuthToken(user, user.getAuthorities());
        authResult.setDetails(authentication.getDetails());

        return authResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthToken.class.isAssignableFrom(authentication);
    }
}
