package lab.zlren.security.core.validate.code.sms;

/**
 * 短信验证码发送接口
 *
 * @author zlren
 * @date 17/10/16
 */
public interface SmsCodeSender {

    /**
     * 发送验证码
     *
     * @param phoneNumber 号码
     * @param code        具体验证码
     */
    void send(String phoneNumber, String code);
}
