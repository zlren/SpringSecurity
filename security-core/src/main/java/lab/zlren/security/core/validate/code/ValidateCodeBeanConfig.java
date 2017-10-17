package lab.zlren.security.core.validate.code;

import lab.zlren.security.core.validate.code.image.DefaultImageCodeGenerator;
import lab.zlren.security.core.validate.code.sms.DefaultSmsCodeSender;
import lab.zlren.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zlren
 * @date 17/10/15
 */
@Configuration
public class ValidateCodeBeanConfig {

    /**
     * 这个方法的名字就是bean的名字
     * 不存在这个bean的时候才用我的默认配置
     * 换句话说如果其他开发者想指定默认实现，要实现ValidateCodeGenerator接口，并且把自己写的实现注册成bean以后的id也得是这个
     * 自己的实现类上需要这样：@Component("defaultImageCodeGenerator")
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        return new DefaultImageCodeGenerator();
    }

    /**
     * 和上面的一个效果
     * 这是第二种实现
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}
