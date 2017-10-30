package lab.zlren.security.app;

import lab.zlren.security.core.properties.OAuth2ClientProperties;
import lab.zlren.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zlren
 * @date 17/10/24
 */
@Configuration
@EnableAuthorizationServer
public class MyAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TokenStore redisTokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(redisTokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {

            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);

            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(enhancers);

            endpoints
                    .tokenEnhancer(tokenEnhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /**
     * 覆盖了这个方法以后，在application.properties中配置的client_id和client_secret就不管用了
     * 与客户端（app或者js前端部分）有关的就都在这里配置
     * 这时候我们写的java-server只给我们自己的APP或者js前端来使用，不涉及开放平台
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // 因为不涉及开放平台所以直接在内存中就好了
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties oAuth2ClientProperties : securityProperties.getOauth2().getClients()) {
                builder
                        .withClient(oAuth2ClientProperties.getClientId())
                        .secret(oAuth2ClientProperties.getClientSecret())
                        .accessTokenValiditySeconds(oAuth2ClientProperties.getAccessTokenValiditySeconds())
                        // refresh_token的有效期设置的长一些，一星期
                        .refreshTokenValiditySeconds(2592000)
                        .authorizedGrantTypes("refresh_token", "password")
                        // 这里配置scope，app请求可以不带scope参数
                        // 如果请求里面带了scope参数，一定是这里配的scope集合里面的一个
                        .scopes("all", "read", "write");
            }
        }
    }
}
