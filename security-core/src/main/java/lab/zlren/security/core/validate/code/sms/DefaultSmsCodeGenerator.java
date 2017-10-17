package lab.zlren.security.core.validate.code.sms;

import lab.zlren.security.core.properties.SecurityProperties;
import lab.zlren.security.core.validate.code.ValidateCode;
import lab.zlren.security.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成工具
 * 这个生成逻辑一般就是这样咯，不用支持让用户自定义，所以直接@Component就可以
 *
 * @author zlren
 * @date 17/10/16
 */
@Component("smsCodeGenerator")
public class DefaultSmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode create(ServletWebRequest servletWebRequest) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new SmsCode(code, securityProperties.getCode().getImage().getExpiredIn());
    }
}
