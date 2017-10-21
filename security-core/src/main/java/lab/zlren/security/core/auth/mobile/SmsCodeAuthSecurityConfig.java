package lab.zlren.security.core.auth.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信登录配置
 * 这个过程就是按照ss的框架来的
 * filter + token + provider
 *
 * @author zlren
 * @date 17/10/20
 */
@Component
public class SmsCodeAuthSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler myAuthSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler myAuthFailureHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        SmsCodeAuthFilter smsCodeAuthFilter = new SmsCodeAuthFilter();
        smsCodeAuthFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
        smsCodeAuthFilter.setAuthenticationSuccessHandler(myAuthSuccessHandler);
        smsCodeAuthFilter.setAuthenticationFailureHandler(myAuthFailureHandler);

        SmsCodeAuthProvider smsCodeAuthProvider = new SmsCodeAuthProvider();
        smsCodeAuthProvider.setUserDetailsService(userDetailsService);

        httpSecurity.authenticationProvider(smsCodeAuthProvider)
                .addFilterAfter(smsCodeAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
