package lab.zlren.security.browser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by zlren on 17/10/14.
 */
@Component
@Slf4j
public class MyUserDetailService implements UserDetailsService {

    // 注入数据库操作类进行具体查询
    // @Autowired
    // private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据用户名查找用户信息
        log.info("登录用户名：{}", username);

        // 这里的 User 是 SS 里面的，它已经实现了 UserDetails 接口，可以直接使用
        return new User(
                username,
                this.passwordEncoder.encode("123456"), // 这个动作应该是注册的时候做的，这里应该从数据库中直接读
                true,
                true,
                true,
                true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
