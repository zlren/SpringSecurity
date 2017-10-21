package lab.zlren.security.browser;

import lab.zlren.security.browser.auth.MyAuthFailureHandler;
import lab.zlren.security.browser.auth.MyAuthSuccessHandler;
import lab.zlren.security.core.auth.mobile.SmsCodeAuthSecurityConfig;
import lab.zlren.security.core.auth.mobile.SmsCodeFilter;
import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.validate.code.ValidateCodeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;


/**
 * SpringSecurity配置类
 * Created by zlren on 17/10/14.
 *
 * @author zlren
 */
@Configuration
@Slf4j
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置信息
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 自定义的登陆成功以后的处理业务
     */
    @Autowired
    private MyAuthSuccessHandler myAuthSuccessHandler;

    /**
     * 同理，自定义登录失败以后的处理逻辑
     */
    @Autowired
    private MyAuthFailureHandler myAuthFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthSecurityConfig smsCodeAuthSecurityConfig;

    @Autowired
    private SpringSocialConfigurer mySpringSocialConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 这里第一次是true，建好表以后再启动就是false，否则会异常
        jdbcTokenRepository.setCreateTableOnStartup(false);

        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 图形验证码拦截器
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(myAuthFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        // 调用它的初始化方法
        validateCodeFilter.afterPropertiesSet();


        // 短信验证码拦截器
        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(myAuthFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        // 调用它的初始化方法
        smsCodeFilter.afterPropertiesSet();


        // 配置在UPFilter前生效
        http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin()
                // 这里是登录页面的url，如果需要验证就转到这里，但实际上为了区别html请求还是数据请求，这里我们指向的是一个controller
                .loginPage("/auth/require")
                // 提交登录数据，交给UsernamePasswordAuthFilter去处理（所以登录页面的form的action处也应该写这个地址）
                .loginProcessingUrl("/auth/form")
                // 用自定义的AuthSuccessHandler去处理验证成功后的逻辑
                .successHandler(myAuthSuccessHandler)
                .failureHandler(myAuthFailureHandler)

                // 记住我
                .and().rememberMe()
                // token持久化数据库
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)

                .and().authorizeRequests()
                // 登录页面其实指向了controller，如果ss需要验证就转到controller上，再由controller决定转到页面还是返回json
                .antMatchers(
                        "/auth/require",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/*"
                ).permitAll()
                .anyRequest()
                .authenticated()

                .and().csrf().disable()

                // 使关于短信验证码的配置生效
                .apply(smsCodeAuthSecurityConfig).and()

                // 社交登录
                .apply(mySpringSocialConfig);
    }
}
