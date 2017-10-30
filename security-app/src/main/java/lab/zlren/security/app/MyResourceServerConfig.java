package lab.zlren.security.app;

import lab.zlren.security.app.auth.MyAuthFailureHandler;
import lab.zlren.security.app.auth.MyAuthSuccessHandler;
import lab.zlren.security.core.auth.mobile.SmsCodeAuthSecurityConfig;
import lab.zlren.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 作为APP的主配置
 *
 * @author zlren
 * @date 17/10/24
 */
@Configuration
@EnableResourceServer
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private MyAuthSuccessHandler myAuthSuccessHandler;

    @Autowired
    private MyAuthFailureHandler myAuthFailureHandler;

    @Autowired
    private SmsCodeAuthSecurityConfig smsCodeAuthSecurityConfig;

    @Autowired
    private SpringSocialConfigurer mySpringSocialConfig;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                formLogin()
                // 这里是登录页面的url，如果需要验证就转到这里，但实际上为了区别html请求还是数据请求，这里我们指向的是一个controller
                .loginPage("/auth/require")
                // 提交登录数据，交给UsernamePasswordAuthFilter去处理（所以登录页面的form的action处也应该写这个地址）
                .loginProcessingUrl("/auth/form")
                // 用自定义的AuthSuccessHandler去处理验证成功后的逻辑
                .successHandler(myAuthSuccessHandler)
                .failureHandler(myAuthFailureHandler).and()

                // 使关于短信验证码的配置生效
                .apply(smsCodeAuthSecurityConfig).and()

                // 社交登录
                .apply(mySpringSocialConfig).and()

                .authorizeRequests()
                // 登录页面其实指向了controller，如果ss需要验证就转到controller上，再由controller决定转到页面还是返回json
                .antMatchers(
                        "/auth/require",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/*",
                        securityProperties.getBrowser().getSignUpUrl(),
                        "/user/regist",
                        "/session/invalid",
                        "/social/signup",
                        securityProperties.getBrowser().getSignOutUrl()
                ).permitAll()

                // 这个路径需要admin角色
                // .antMatchers("/user").hasRole("ADMIN")
                // .antMatchers(HttpMethod.GET, "/user/*").hasRole("ADMIN")

                .anyRequest().authenticated().and()

                .csrf().disable();
    }
}
