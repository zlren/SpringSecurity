package lab.zlren.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lab.zlren.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zlren
 * @date 17/10/11
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    /**
     * 拿到当前用户信息
     *
     * @return
     */
    @GetMapping("me")
    public Object getCurrentUser(Authentication authentication) {
        // SecurityContextHolder.getContext().getAuthentication(); 这句话可以拿到当前用户
        // 简单的方法就是直接在方法参数里面增加Authentication参数，它就是
        return authentication;
    }

    @GetMapping("detail")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetail) {
        // 这里只拿到了authentication里面的userdetail部分
        return userDetail;
    }

    /**
     * 查询用户列表
     *
     * @param username
     * @return
     */
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation("查询用户列表")
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
    public User queryUserInfo(@ApiParam("用户id") @PathVariable String id) {

        // throw new UserNotExistException(id);

        return new User().setUsername("tom");
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        log.info("{}", user);

        // if (bindingResult.hasErrors()) {
        //     bindingResult.getAllErrors().forEach(error -> log.info("{}", error.getDefaultMessage()));
        // }

        return user.setId("1");
    }

    @PutMapping("{id}")
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.info("{}", error.getDefaultMessage()));
        }

        log.info("{}", user);

        return user.setUsername("zlren");
    }


    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable String id) {
        log.info("删除会员：{}", id);
    }
}
