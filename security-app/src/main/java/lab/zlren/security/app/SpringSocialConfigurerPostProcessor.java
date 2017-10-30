package lab.zlren.security.app;

import lab.zlren.security.core.social.qq.MySpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 在app环境下，社交账号首次登录以后不要跳转到注册页面
 * 在Spring容器里面在所有的bean初始化之前和之后调用
 *
 * @author zlren
 * @date 17/10/25
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    /**
     * 修改配置
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (StringUtils.equals("mySpringSocialConfig", beanName)) {
            MySpringSocialConfigurer mySpringSocialConfigurer = (MySpringSocialConfigurer) bean;
            mySpringSocialConfigurer.signupUrl("/social/signup");
            return mySpringSocialConfigurer;
        }

        return bean;
    }
}
