package lab.zlren.security.core.validate.code;


import org.springframework.security.core.AuthenticationException;

/**
 * 继承的这个异常是验证过程中所有异常的基类，这样会被捕捉到
 *
 * @author zlren
 * @date 17/10/15
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
