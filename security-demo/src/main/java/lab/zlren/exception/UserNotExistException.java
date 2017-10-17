package lab.zlren.exception;

import lombok.Data;

/**
 * @author zlren
 * @date 17/10/12
 */
@Data
public class UserNotExistException extends RuntimeException {

    private String id;

    public UserNotExistException(String id) {
        super("user not exist");
        this.id = id;
    }
}
