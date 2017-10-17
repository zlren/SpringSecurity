package lab.zlren.web.async;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zlren
 * @date 17/10/13
 */
@Component
@Data
public class DeferredResultHolder {
    private Map<String, DeferredResult<String>> map = new HashMap<>();
}
