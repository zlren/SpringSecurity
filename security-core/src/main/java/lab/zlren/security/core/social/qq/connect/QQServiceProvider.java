package lab.zlren.security.core.social.qq.connect;

import lab.zlren.security.core.social.qq.api.QQApi;
import lombok.Setter;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @author zlren
 * @date 17/10/21
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQApi> {

    /**
     * 用户导向认证服务器的url
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";


    /**
     * 拿着授权码去申请token的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";


    /**
     * 构造函数
     *
     * @param appId
     * @param appSecret
     */
    public QQServiceProvider(String appId, String appSecret) {
        // 这里的appId和appSecret是开发的这个应用在QQ上的标识id和密码，这样QQ才知道是我们这个应用在申请
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));

        this.appId = appId;
    }

    @Setter
    private String appId;

    @Override
    public QQApi getApi(String accessToken) {
        return new QQApi(accessToken, appId);
    }
}
