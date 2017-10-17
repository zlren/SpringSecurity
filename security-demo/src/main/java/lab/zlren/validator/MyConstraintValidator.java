package lab.zlren.validator;

import lab.zlren.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zlren
 * @date 17/10/12
 */
@Slf4j
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, String> {

    /**
     * 这里可以注入组件完成校验逻辑
     */
    @Autowired
    private HelloService helloService;

    @Override
    public void initialize(MyConstraint myConstraint) {
        log.info("自定义校验器初始化");
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        log.info("被校验值：{}", s);
        this.helloService.greeting(s);
        return false;
    }
}
