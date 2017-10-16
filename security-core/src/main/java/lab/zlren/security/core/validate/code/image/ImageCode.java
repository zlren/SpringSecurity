package lab.zlren.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * Created by zlren on 17/10/15.
 */
@Data
@AllArgsConstructor
public class ImageCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime; // 过期的时间点

    /**
     * @param image
     * @param code
     * @param expireIn 过期时间段，单位秒
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
