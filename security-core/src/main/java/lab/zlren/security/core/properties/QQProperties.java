package lab.zlren.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author zlren
 * @date 17/10/21
 */

public class QQProperties extends SocialProperties {

    @Getter
    @Setter
    private String providerId = "qq";
}
