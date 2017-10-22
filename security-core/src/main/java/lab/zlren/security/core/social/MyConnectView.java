package lab.zlren.security.core.social;


import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 绑定社交账号后跳转的视图
 *
 * @author zlren
 * @date 17/10/22
 */
// @Component("connect/weixinConnected") 不建议这么配置，因为想重用，而不仅仅是给微信的绑定结果用
public class MyConnectView extends AbstractView {

    /**
     * 绑定好后是从微信的授权页面跳回来的，所以应该返回一个页面
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        // 返回一个页面
        response.setContentType("text/html;charset=UTF-8");
        // 绑定和解绑的差别
        if (model.get("connection") == null) {
            response.getWriter().write("<h4>解绑成功</h4>");
        } else {
            response.getWriter().write("<h4>绑定成功</h4>");
        }

    }
}
