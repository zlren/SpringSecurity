package lab.zlren.security.core.validate.code.image;

import lab.zlren.security.core.validate.code.ValidateCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图片验证码封装类
 *
 * @author zlren
 * @date 17/10/15
 */
public class ImageCode extends ValidateCode {

    @Getter
    @Setter
    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }
}
