package com.zizhou.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.pojo.Permission;
import com.zizhou.pojo.Role;
import com.zizhou.pojo.User;
import com.zizhou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 认证:根据用户名从数据库查询密码提交给框架,密码是否正确是由框架进行对比的
 * 授权:根据用户查询角色和权限数据,授权给当前登录的用户
 * @Author: NickXia
 * @date: 2020/8/7 16:43
 */
@Component
public class SecurityUserService implements UserDetailsService {

    //注入密码加密对象,为明文密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Reference
    private UserService userService;

    /**
     * 根据用户名加载用户对象
     * @param username 页面输入用户名,传入此方法中
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询数据库,获得User
        User user = userService.findUserByUsername(username);
        //2.判断是否为null
        if(user == null){
            return null;
        }

        //3.把用户名,数据库查询出来的密码以及权限访问,创建userDetails对象返回
        String dbPassword = user.getPassword();
        //先把角色和权限写死,后面从数据库中查询出来
        List<GrantedAuthority> list = new ArrayList<>();

        Set<Role> roles = userService.findRoleByUserId(user.getId());
        if (roles != null && roles.size() > 0){
            for (Role role : roles) {
                String keyword = role.getKeyword();//获取角色关键字
                list.add(new SimpleGrantedAuthority(keyword));//为角色授予权限
                Set<Permission> permissions = role.getPermissions();//获取所有的权限,粒度最细
                if (permissions != null && permissions.size() > 0){
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));//授予单个权限 例如:删除检查项...
                    }
                }
            }
        }
        return new  org.springframework.security.core.userdetails.User(username,dbPassword,list);
    }
}
