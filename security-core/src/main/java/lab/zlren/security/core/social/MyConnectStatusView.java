package lab.zlren.security.core.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringSocial中会将用户的社交账号绑定情况返回给一个视图
 * 这是我们自己写的这个视图，拿到数据并呈现
 * 注意@Component中写的是视图的名称
 * <p>
 * {@link ConnectController#connectionStatus(NativeWebRequest, Model)}
 *
 * @author zlren
 * @date 17/10/22
 */
@Component("connect/status")
public class MyConnectStatusView extends AbstractView {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // connectionMap是SpringSocial规定的z
        // 从这个map中拿到的信息就是qq有没有绑定、微信有没有绑定等等这些信息
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) model.get("connectionMap");

        Map<String, Boolean> result = new HashMap<>();
        for (String key : connections.keySet()) {
            // weixin: true
            // qq: false
            result.put(key, CollectionUtils.isNotEmpty(connections.get(key)));
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
