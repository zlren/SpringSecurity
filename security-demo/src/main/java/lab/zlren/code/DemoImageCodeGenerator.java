package lab.zlren.code;

import lab.zlren.security.core.validate.code.image.ImageCode;
import lab.zlren.security.core.validate.code.ValidateCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author zlren
 * @date 17/10/15
 */
// @Component("imageCodeGenerator")
@Slf4j
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ImageCode create(ServletWebRequest servletWebRequest) {
        log.info("自定义图形验证码生成");
        return null;
    }
}
