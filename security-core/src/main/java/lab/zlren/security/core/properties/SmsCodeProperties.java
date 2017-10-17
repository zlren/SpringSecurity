package lab.zlren.security.core.properties;

import lombok.Data;

/**
 * 短信验证码配置类
 *
 * @author zlren
 * @date 17/10/15
 */
@Data
public class SmsCodeProperties {

    private int length = 6;
    private int expiredIn = 60;
}
