package lab.zlren.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 17/10/12
 */
@Service
@Slf4j
public class HelloService {

    public String greeting(String name) {
        log.info("greeting {}", name);
        return "hello " + name;
    }
}
