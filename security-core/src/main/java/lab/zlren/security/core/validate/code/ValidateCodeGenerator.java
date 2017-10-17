package lab.zlren.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码生成器
 *
 * @author zlren
 */
public interface ValidateCodeGenerator {

    /**
     * 用户创建验证码，包括短信验证码和图形验证码
     *
     * @param servletWebRequest
     * @return
     */
    ValidateCode create(ServletWebRequest servletWebRequest);
}
