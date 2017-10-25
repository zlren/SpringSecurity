package lab.zlren.security.browser.support;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zlren
 * @date 17/10/22
 */
@Data
@Accessors(chain = true)
public class SocialUserInfo {

    /**
     * 服务商id
     */
    private String providerId;

    /**
     * openId
     */
    private String providerUserId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headimg;
}
