package lab.zlren.security.core.properties;

import lombok.Data;

/**
 * 验证码的校验
 * 包括图片验证码和短信验证码
 *
 * @author zlren
 * @date 17/10/15
 */
@Data
public class ValidateCodeProperties {
    private ImageCodeProperties image = new ImageCodeProperties();
    private SmsCodeProperties sms = new SmsCodeProperties();
}
