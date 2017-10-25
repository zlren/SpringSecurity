package lab.zlren.security.browser.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author zlren
 * @date 17/10/22
 */
@Slf4j
public class MyExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    /**
     * 处理session超时的事件
     *
     * @param sessionInformationExpiredEvent 从这里面可以拿到细化的事件和信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws
            IOException, ServletException {
        sessionInformationExpiredEvent.getResponse().setContentType("application/json;charset=UTF-8");
        sessionInformationExpiredEvent.getResponse().getWriter().write("并发登录！！");
    }
}
