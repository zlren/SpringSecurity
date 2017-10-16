package lab.zlren.security.core.validate.code.sms;

/**
 * 短信验证码发送接口
 * Created by zlren on 17/10/16.
 */
public interface SmsCodeSender {
    void send(String phoneNumber, String code);
}
