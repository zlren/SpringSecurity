package lab.zlren.security.core.social.qq.connect;

import lab.zlren.security.core.social.qq.api.QQApi;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author zlren
 * @date 17/10/21
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQApi> {

    /**
     * @param providerId 这是什么？这是QQ这个服务提供商对于我开发的这个应用的id
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
