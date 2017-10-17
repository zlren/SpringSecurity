package lab.zlren.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zlren
 * @date 17/10/16
 */
@Data
@AllArgsConstructor
public class ValidateCode {

    private String code;
    /**
     * 过期的时间点
     */
    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
