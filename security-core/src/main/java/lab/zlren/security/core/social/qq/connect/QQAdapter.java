package lab.zlren.security.core.social.qq.connect;

import lab.zlren.security.core.social.qq.api.QQApi;
import lab.zlren.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @author zlren
 * @date 17/10/21
 */
public class QQAdapter implements ApiAdapter<QQApi> {

    /**
     * QQ服务提供商还可不可用
     * 这里直接简单一点认为可用
     *
     * @param qqApi
     * @return
     */
    @Override
    public boolean test(QQApi qqApi) {
        return true;
    }

    /**
     * 这个是做适配器的主要工作，从QQUserInfo中拿出数据放在connectionValues中，去适配SpringSocial
     *
     * @param qqApi
     * @param connectionValues
     */
    @Override
    public void setConnectionValues(QQApi qqApi, ConnectionValues connectionValues) {

        QQUserInfo userInfo = qqApi.getUserInfo();

        connectionValues.setDisplayName(userInfo.getNickname());
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        // 如果是微博就是主页url，但是qq没有
        connectionValues.setProfileUrl(null);
        // 这是用户在QQ里面的唯一标识
        connectionValues.setProviderUserId(userInfo.getOpenId());
    }

    /**
     * @param qqApi
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQApi qqApi) {
        return null;
    }

    /**
     * @param qqApi
     * @param s
     */
    @Override
    public void updateStatus(QQApi qqApi, String s) {

    }
}
