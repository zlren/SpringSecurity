package lab.zlren.security.demo.exception;

import lombok.Data;

/**
 * Created by zlren on 17/10/12.
 */
@Data
public class UserNotExistException extends RuntimeException {

    private String id;

    public UserNotExistException(String id) {
        super("user not exist");
        this.id = id;
    }
}
