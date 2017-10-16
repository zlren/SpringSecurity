package lab.zlren.security.core.properties;

import lombok.Data;

/**
 * Created by zlren on 17/10/15.
 */
@Data
public class ImageCodeProperties {
    // 这些是默认配置
    private int width = 67;
    private int height = 23;
    private int length = 4;
    private int expiredIn = 60;
    private String url; // 需要校验验证码的url
}
