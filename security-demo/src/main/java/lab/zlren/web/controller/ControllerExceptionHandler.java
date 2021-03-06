package lab.zlren.web.controller;

import lab.zlren.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zlren
 * @date 17/10/12
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    // 专门处理Controller中出现的UserNotException异常
    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleUserNotException(UserNotExistException ex) {

        // 返回的数据
        Map<String, Object> result = new HashMap<>(2);
        result.put("id", ex.getId());
        result.put("msg", ex.getMessage());

        return result;
    }
}
