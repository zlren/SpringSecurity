package lab.zlren.security.core.validate.code.sms;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认验证码发送器
 *
 * @author zlren
 * @date 17/10/16
 */
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String phoneNumber, String code) {
        log.info("向手机：{} 发送验证码：{}", phoneNumber, code);
    }
}
