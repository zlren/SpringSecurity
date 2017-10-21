package lab.zlren.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 自定义UserDetailService
 *
 * @author zlren
 * @date 17/10/14
 */
@Component
@Slf4j
public class MyUserDetailServiceImpl implements UserDetailsService, SocialUserDetailsService {

    // 注入数据库操作类进行具体查询
    // @Autowired
    // private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 表单登录的时候用到
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据用户名查找用户信息
        log.info("登录用户名：{}", username);

        // 这里的 User 是 SS 里面的，它已经实现了 UserDetails 接口，可以直接使用
        return new User(
                username,
                // 这个动作应该是注册的时候做的，这里应该从数据库中直接读
                this.passwordEncoder.encode("123456"),
                true,
                true,
                true,
                true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }


    /**
     * 在社交登录的时候用到
     *
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        // 根据用户名查找用户信息
        log.info("登录用户名：{}", userId);

        // 这里的 User 是 SS 里面的，它已经实现了 UserDetails 接口，可以直接使用
        return new SocialUser(
                userId,
                // 这个动作应该是注册的时候做的，这里应该从数据库中直接读
                this.passwordEncoder.encode("123456"),
                true,
                true,
                true,
                true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
