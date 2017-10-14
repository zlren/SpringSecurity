package lab.zlren.security.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by zlren on 17/10/12.
 */
@Service
@Slf4j
public class HelloService {

    public String greeting(String name) {
        log.info("greeting {}", name);
        return "hello " + name;
    }
}
