package lab.zlren.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zlren
 * @date 17/10/12
 */
public class UserNotExistException extends RuntimeException {

    @Getter
    @Setter
    private String id;

    public UserNotExistException(String id) {
        super("user not exist");
        this.id = id;
    }
}
