package lab.zlren.security.core.social.weixin.connect;

import lab.zlren.security.core.social.weixin.api.WeixinApi;
import lab.zlren.security.core.social.weixin.api.WeixinUserInfo;
import lombok.Setter;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @author zlren
 * @date 17/10/22
 */
public class WeixinAdapter implements ApiAdapter<WeixinApi> {


    /**
     * 唯一与QQ不同的就是微信的openId放在了这里，而没有在WeixinApi那里
     */
    @Setter
    private String openId;

    public WeixinAdapter() {
    }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    /**
     * @param api
     * @return
     */
    @Override
    public boolean test(WeixinApi api) {
        return true;
    }

    /**
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(WeixinApi api, ConnectionValues values) {
        WeixinUserInfo profile = api.getUserInfo(openId);
        values.setProviderUserId(profile.getOpenid());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getHeadimgurl());
    }

    /**
     * @param api
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(WeixinApi api) {
        return null;
    }

    /**
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(WeixinApi api, String message) {
        //do nothing
    }
}
