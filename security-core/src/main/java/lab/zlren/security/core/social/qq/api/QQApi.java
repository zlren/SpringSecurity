package lab.zlren.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * 多例的，没有注解成@Component，否则accessToken会有问题
 *
 * @author zlren
 * @date 17/10/21
 */
@Slf4j
public class QQApi extends AbstractOAuth2ApiBinding {

    /**
     * 获取用户openid的url，什么是openid？用户在qq里面的唯一标识
     * todo 为什么这里父类不帮我们带上access_token呢？
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";


    /**
     * 根据appid和openid去获取用户信息
     * 实际上还有一个参数是accesstoken，不过父类会帮我们加上这个参数，就不用自己加了
     */
    private static final String URL_GET_USERINFO =
            "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";


    @Setter
    private String appId;

    private String openId;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param accessToken 走完认证流程以后会拿到
     * @param appId       自己为这个应用申请的
     */
    public QQApi(String accessToken, String appId) {

        // 传token的策略是放在请求url中的，默认的会放在header中，这不是qq想要的
        // 同时如果指定策略为parameter，在使用父类的restTemplate发请求的时候，会自动带上access_token
        // 这也是我们在URL_GET_OPENID中没有传access_token的原因，实际上qq要求传，但是父类帮我们做了
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);

        log.info(result);

        // {"client_id": "YOUR_APPID", "openid": "YOUR_OPENID"}
        // 这样写只是快速的实现功能
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");

        log.info("openid是 {}", openId);
    }


    /**
     * 从服务提供商里面拿到用户的数据并包装
     *
     * @return
     */
    public QQUserInfo getUserInfo() {

        String url = String.format(URL_GET_USERINFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);

        QQUserInfo qqUserInfo;
        try {

            qqUserInfo = objectMapper.readValue(result, QQUserInfo.class);
            qqUserInfo.setOpenId(openId);

            return qqUserInfo;

        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
