package lab.zlren.security.core.properties;

import lombok.Data;

/**
 * 这些是默认配置
 *
 * @author zlren
 * @date 17/10/15
 */
@Data
public class ImageCodeProperties {

    private int width = 67;
    private int height = 23;
    private int length = 4;
    private int expiredIn = 60;

    /**
     * 需要校验验证码的url
     */
    private String url;
}
