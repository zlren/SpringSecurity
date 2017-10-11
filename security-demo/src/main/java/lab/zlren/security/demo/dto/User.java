package lab.zlren.security.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by zlren on 17/10/11.
 */
@Data
@Accessors(chain = true)
public class User {

    public interface UserSimpleView {

    }

    public interface UserDetailView extends UserSimpleView {

    }

    @JsonView(UserSimpleView.class) // 这里只指定了simple，但是detail继承simple，所以detail也会显示username
    private String username;

    @JsonView(UserDetailView.class)
    private String password;
}
