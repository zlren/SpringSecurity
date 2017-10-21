package lab.zlren.security.core.social.qq.connect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author zlren
 * @date 17/10/21
 */
@Slf4j
public class QQAuth2Template extends OAuth2Template {

    /**
     * {@link org.springframework.social.oauth2.OAuth2Template#exchangeCredentialsForAccess}
     *
     * @param clientId
     * @param clientSecret
     * @param authorizeUrl
     * @param accessTokenUrl
     */
    public QQAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        // 当它为true才会带上client_id和client_secret参数
        // exchangeCredentialsForAccess()
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 自带的template不能处理返回的授权码的请求在回调的时候的请求类型为text/html形式
     * 自带的期望为json形式
     * 而QQ是text形式的
     *
     * @return
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }


    /**
     * 请求token，将token组装成AccessGrant
     * 因为默认的和qq的实现形式有出入，qq返回的不是json串而是&隔开的字符串，这里我们需要自己操作
     *
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {

        String responseString = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        log.info("获取access_token的响应: {}", responseString);

        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseString, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expireIn = Long.valueOf(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");


        return new AccessGrant(accessToken, null, refreshToken, expireIn);
    }
}
