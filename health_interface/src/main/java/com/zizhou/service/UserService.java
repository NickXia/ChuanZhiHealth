package com.zizhou.service;

import com.zizhou.pojo.Role;
import com.zizhou.pojo.User;

import java.util.Set;

/**
 * @Description: 用户服务接口
 * @Author: NickXia
 * @date: 2020/8/8 13:04
 */
public interface UserService {
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
     User findUserByUsername(String username);

    /**
     * 根据用户id查询用户对象(包含角色信息)
     * @param id
     * @return
     */
    Set<Role> findRoleByUserId(Integer id);
}
