package lab.zlren.security.app.social;

import lab.zlren.security.app.exception.AppSecretException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * @author zlren
 * @date 17/10/25
 */
@Component
public class AppSignUpUtils {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * 保存到redis中
     *
     * @param webRequest
     * @param connectionData 第三方用户信息
     */
    public void saveConnectionData(WebRequest webRequest, ConnectionData connectionData) {
        // 存储
        redisTemplate.opsForValue().set(getKey(webRequest), connectionData, 10, TimeUnit.MINUTES);
    }

    /**
     * 负责做绑定
     *
     * @param request
     * @param userId
     */
    public void doPostSignUp(WebRequest request, String userId) {

        String key = getKey(request);
        if (!redisTemplate.hasKey(key)) {
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }

        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
                .createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);

        redisTemplate.delete(key);
    }

    private String getKey(WebRequest webRequest) {

        String deviceId = webRequest.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new AppSecretException("设备id参数不能为空");
        }

        return "security:social.connect." + deviceId;
    }
}
