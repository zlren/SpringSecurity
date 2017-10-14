package lab.zlren.web.async;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列
 * place模拟请求
 * complete模拟结果
 * Created by zlren on 17/10/13.
 */
@Data
@Slf4j
@Component
public class MockQueue {
    private String placeOrder;
    private String completeOrder;

    public void setPlaceOrder(String placeOrder) throws InterruptedException {
        new Thread(() -> {
            log.info("接到下单请求");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;
            log.info("下单请求处理完毕，{}", this.completeOrder);
        }).start();
    }

}
