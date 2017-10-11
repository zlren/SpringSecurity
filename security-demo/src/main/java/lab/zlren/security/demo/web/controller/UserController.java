package lab.zlren.security.demo.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lab.zlren.security.demo.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlren on 17/10/11.
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> queryUserList(@RequestParam String username) {

        log.info("username是：{}", username);

        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        return userList;
    }

    @GetMapping("{id:\\d+}") // url中使用正则表达式进行限定
    @JsonView(User.UserDetailView.class)
    public User queryUserInfo(@PathVariable String id) {
        return new User().setUsername("tom");
    }
}
