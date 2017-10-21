package lab.zlren.security.core.properties;

import lombok.Data;

/**
 * @author zlren
 * @date 17/10/21
 */
@Data
public class SocialProperties {
    private QQProperties qq = new QQProperties();
    private String filterProcessesUrl = "/auth";
}
