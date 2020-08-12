package com.zizhou.security;

import com.zizhou.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: UserDetailsService接口实现类
 * 认证:根据用户名从数据库查询密码提交给框架,密码是否正确是由框架进行对比的
 * 授权:根据用户查询角色和权限数据,授权给当前登录的用户
 * @Author: NickXia
 * @date: 2020/8/7 16:43
 */
public class SpringSecurityUserService implements UserDetailsService {

    //注入密码加密对象,为明文密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 根据用户名加载用户对象
     * @param username 页面输入用户名,传入此方法中
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询数据库,获得User
        User user = findUserByname(username);
        //2.判断是否为null
        if(user == null){
            return null;
        }

        //3.把用户名,数据库查询出来的密码以及权限访问,创建userDetails对象返回
        String dbPassword = user.getPassword();
        //先把角色和权限写死,后面从数据库中查询出来
        List<GrantedAuthority> list = new ArrayList<>();
        //角色表中查询keyword
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        //权限表中查询keyword add
        list.add(new SimpleGrantedAuthority("add"));
        //权限表中查询keyword delete
        list.add(new SimpleGrantedAuthority("delete"));

        //4.用户对象存在 new User(xxxxx) 注意：此User是框架中的用户对象
        //String username:用户名username, String password：从数据库查询出来的密码
        // Collection<? extends GrantedAuthority> authorities 权限列表
        //return new  org.springframework.security.core.userdetails.User(username,"{noop}"+dbPassword,list);
        return new  org.springframework.security.core.userdetails.User(username,dbPassword,list);
    }

    /**
     * 模拟从数据库中查询(根据username)
     * @param username
     * @return
     */
    private User findUserByname(String username) {
        if("admin".equals(username)){
            User user = new User();
            user.setUsername(username);
            //encoder.encode()  // encode框架内部方法会随机产生salt,最终每次加密的密码都不一样,相当安全
            //encoder.matches() //密码匹配的
            user.setPassword(encoder.encode("123456"));//存入到数据库的密码一定是加密后的
            return user;
        }
        return null;
    }
}
