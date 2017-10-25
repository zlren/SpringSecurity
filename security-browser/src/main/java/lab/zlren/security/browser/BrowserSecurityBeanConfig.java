package lab.zlren.security.browser;

import lab.zlren.security.browser.logout.MyLogoutSuccessHandler;
import lab.zlren.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author zlren
 * @date 17/10/23
 */
@Configuration
public class BrowserSecurityBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(MyLogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new MyLogoutSuccessHandler(securityProperties);
    }
}
