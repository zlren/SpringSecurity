package lab.zlren.security.core.social;

import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.social.qq.MySpringSocialConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @author zlren
 * @date 17/10/21
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        // 第一个参数是数据源
        // 第二个参数是..
        // 第三个参数用来加解密，这里比较简单没有做任何加密操作
        JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        // 在这里可以设置一些表名前缀
        jdbcUsersConnectionRepository.setTablePrefix("");

        if (connectionSignUp != null) {
            jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }

        return jdbcUsersConnectionRepository;
    }

    @Bean
    public SpringSocialConfigurer mySpringSocialConfig() {

        MySpringSocialConfigurer mySpringSocialConfigurer = new MySpringSocialConfigurer(securityProperties.getSocial
                ().getFilterProcessesUrl());
        // 设置我们自己的sign_up地址
        mySpringSocialConfigurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());

        return mySpringSocialConfigurer;
    }

    /**
     * @param connectionFactoryLocator Spring会注入这个参数
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository
                (connectionFactoryLocator));
    }
}
