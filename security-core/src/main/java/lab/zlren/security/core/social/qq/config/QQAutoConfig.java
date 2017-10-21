package lab.zlren.security.core.social.qq.config;

import lab.zlren.security.core.properties.QQProperties;
import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @author zlren
 * @date 17/10/21
 */
@Configuration
// 只有lab.zlren.security.social.qq.app-id被配置了，这个QQAutoConfig才生效
@ConditionalOnProperty(prefix = "lab.zlren.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qq = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
    }
}
