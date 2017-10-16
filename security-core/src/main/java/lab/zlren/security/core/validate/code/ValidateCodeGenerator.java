package lab.zlren.security.core.validate.code.generator;

import lab.zlren.security.core.validate.code.ImageCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码生成器
 */
public interface ValidateCodeGenerator {
    public ImageCode createImageCode(ServletWebRequest servletWebRequest);
}
