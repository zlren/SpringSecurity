package lab.zlren.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lab.zlren.validator.MyConstraint;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

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

    @JsonView(UserSimpleView.class)
    private String id;

    @JsonView(UserSimpleView.class) // 这里只指定了simple，但是detail继承simple，所以detail也会显示username
    @MyConstraint(message = "这是一个测试")
    @ApiModelProperty("这是用户名")
    private String username;

    @JsonView(UserDetailView.class)
    @NotBlank(message = "密码不能为空")
    private String password;

    @JsonView(UserSimpleView.class)
    private Date birthday;
}
