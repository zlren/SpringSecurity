package lab.zlren.security.core.social.weixin.connect;

import lab.zlren.security.core.social.weixin.api.WeixinApi;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 微信连接工厂
 *
 * @author zlren
 * @date 17/10/22
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<WeixinApi> {

    /**
     * @param appId
     * @param appSecret
     */
    public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeixinServiceProvider(appId, appSecret), new WeixinAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinAccessGrant) {
            return ((WeixinAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }


    /**
     * @param accessGrant
     * @return
     */
    @Override
    public Connection<WeixinApi> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<>(
                getProviderId(),
                extractProviderUserId(accessGrant),
                accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(),
                accessGrant.getExpireTime(),
                getOAuth2ServiceProvider(),
                // 注意adapter这里
                getApiAdapter(extractProviderUserId(accessGrant)));
    }

    /**
     * @param data
     * @return
     */
    @Override
    public Connection<WeixinApi> createConnection(ConnectionData data) {
        return new OAuth2Connection<>(
                data,
                getOAuth2ServiceProvider(),
                getApiAdapter(data.getProviderUserId()));
    }


    /**
     * 这里是new一个Adapter的实例返回，因为实例中包含了openId，而每个用户的openId不同，所以像adapter这个本来作为工具类的东西也得多例化
     *
     * @param providerUserId
     * @return
     */
    private ApiAdapter<WeixinApi> getApiAdapter(String providerUserId) {
        return new WeixinAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<WeixinApi> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeixinApi>) getServiceProvider();
    }
}
