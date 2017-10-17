package lab.zlren.security.core.validate.code.sms;

import lab.zlren.security.core.validate.code.ValidateCode;

import java.time.LocalDateTime;

/**
 * 短信验证码封装类
 *
 * @author zlren
 * @date 17/10/15
 */

public class SmsCode extends ValidateCode {

    public SmsCode(String code, LocalDateTime expireTime) {
        super(code, expireTime);
    }

    SmsCode(String code, int expireIn) {
        super(code, expireIn);
    }
}
