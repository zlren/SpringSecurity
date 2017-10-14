package lab.zlren.security.demo.web.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 模拟监听队列
 */
@Component
@Slf4j
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> { // Spring容器初始化完毕事件

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 当初始化完毕以后需要做的事
        new Thread(() -> {
            while (true) {

                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {

                    String orderNumber = mockQueue.getCompleteOrder();
                    log.info("返回订单处理结果:{}", orderNumber);
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success"); // 这里的值会返回给前端
                    mockQueue.setCompleteOrder(null);

                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
