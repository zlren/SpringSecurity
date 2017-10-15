package lab.zlren.security.browser;

import lab.zlren.security.browser.auth.MyAuthFailureHandler;
import lab.zlren.security.browser.auth.MyAuthSuccessHandler;
import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.validate.code.ValidateCodeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置类
 * Created by zlren on 17/10/14.
 */
@Configuration
@Slf4j
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    // 配置信息
    private SecurityProperties securityProperties;

    @Autowired
    // 自定义的登陆成功以后的处理业务
    private MyAuthSuccessHandler myAuthSuccessHandler;

    @Autowired
    // 同理，自定义登录失败以后的处理逻辑
    private MyAuthFailureHandler myAuthFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 验证码拦截器
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(myAuthFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet(); // 调用它的初始化方法

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 配置在UPFilter前生效
                .formLogin()
                .loginPage("/auth/require") // 这里是登录页面的url，如果需要验证就转到这里，但实际上为了区别html请求还是数据请求，这里我们指向的是一个controller
                .loginProcessingUrl("/auth/form") // 提交登录数据，交给UsernamePasswordAuthFilter去处理（所以登录页面的form的action处也应该写这个地址）
                .successHandler(myAuthSuccessHandler) // 用自定义的AuthSuccessHandler去处理验证成功后的逻辑
                .failureHandler(myAuthFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/auth/require", // 登录页面其实指向了controller，如果ss需要验证就转到controller上，再由controller决定转到页面还是返回json
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/image"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable();
    }
}
