package lab.zlren.security.core.properties;

import lombok.Data;

/**
 * @author zlren
 * @date 17/10/15
 */
@Data
public class BrowserProperties {

    /**
     * 默认登录页，用户自定义后会抹掉此配置
     */
    private String loginPage = "/sign_in.html";

    /**
     * 默认注册页，需要用户自定义
     */
    private String signUpUrl = "/sign_up.html";

    /**
     * 默认返回json
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 记住我有效期
     */
    private int rememberMeSeconds = 3600;
}
