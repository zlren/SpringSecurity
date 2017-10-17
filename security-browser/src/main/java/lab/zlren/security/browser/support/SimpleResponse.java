package lab.zlren.security.browser.support;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 简单包装类，作为返回给前端的对象
 *
 * @author zlren
 * @date 17/10/15
 */
@Data
@AllArgsConstructor
public class SimpleResponse {
    private Object content;
}
