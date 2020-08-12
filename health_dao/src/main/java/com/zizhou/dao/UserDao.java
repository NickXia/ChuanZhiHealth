package com.zizhou.dao;

import com.zizhou.pojo.User;

/**
 * @Description: 用户dao接口
 * @Author: NickXia
 * @date: 2020/8/8 13:16
 */
public interface UserDao {

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
     User findUserByUsername(String username);
}
