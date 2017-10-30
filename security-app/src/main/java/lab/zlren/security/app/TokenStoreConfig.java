package lab.zlren.security.app;

import lab.zlren.security.app.jwt.MyJwtTokenEnhancer;
import lab.zlren.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 将token存储在redis中
 *
 * @author zlren
 * @date 17/10/26
 */
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @ConditionalOnProperty(prefix = "lab.zlren.security.oauth2", name = "storeType", havingValue = "redis")
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * prefix.name 是要被检查的属性key
     * 当lab.zlren.security.oauth2.storeType的值为jwt的时候，启动这个配置
     * 当我在配置文件中根本没有配上面那个key的时候我也认为是匹配的
     */
    @Configuration
    @ConditionalOnProperty(prefix = "lab.zlren.security.oauth2", name = "storeType", havingValue = "jwt",
            matchIfMissing = true)
    public static class JwtTokenConfig {

        @Autowired
        private SecurityProperties securityProperties;

        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
            return jwtAccessTokenConverter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer() {
            return new MyJwtTokenEnhancer();
        }

    }
}
