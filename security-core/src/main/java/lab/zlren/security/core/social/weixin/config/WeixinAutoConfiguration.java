package lab.zlren.security.core.social.weixin.config;

import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.properties.WeixinProperties;
import lab.zlren.security.core.social.MyConnectView;
import lab.zlren.security.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * 微信登录配置
 *
 * @author zlren
 * @date 17/10/22
 */
@Configuration
@ConditionalOnProperty(prefix = "lab.zlren.security.social.weixin", name = "app-id")
public class WeixinAutoConfiguration extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * @return
     */
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
                weixinConfig.getAppSecret());
    }

    // 这里可以配置多个名字从而提高重用性
    @Bean({"connect/weixinConnect", "connect/weixinConnected"})
    // 使用这个容器的开发者可以自定义这个视图从而抹掉此配置
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView() {
        return new MyConnectView();
    }
}
