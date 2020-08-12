package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zizhou.dao.PermissionDao;
import com.zizhou.dao.RoleDao;
import com.zizhou.dao.UserDao;
import com.zizhou.pojo.Permission;
import com.zizhou.pojo.Role;
import com.zizhou.pojo.User;
import com.zizhou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @Description: 用户服务接口实现类
 * @Author: NickXia
 * @date: 2020/8/8 13:08
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    /**
     * 根据用户id查询用户对象(包含角色 权限)
     * @param userId
     * @return
     */
    @Override
    public Set<Role> findRoleByUserId(Integer userId) {
        //1.根据用户id查询角色数据
        Set<Role> roleSet = roleDao.findRoleByUserId(userId);

        //2.根据角色查询权限数据
        if (roleSet != null && roleSet.size() > 0){
            for (Role role : roleSet) {
                Integer roleId = role.getId();//获得角色id
                //根据角色id获取权限数据
                Set<Permission> permissionSet = permissionDao.findPermissionByRoleId(roleId);
                role.setPermissions(permissionSet);//将角色与权限关联
            }
        }

        return roleSet;
    }
}
