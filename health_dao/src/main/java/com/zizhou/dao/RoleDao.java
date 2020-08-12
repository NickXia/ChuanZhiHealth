package com.zizhou.dao;

import com.zizhou.pojo.Role;

import java.util.Set;

/**
 * @Description: 角色dao接口
 * @Author: NickXia
 * @date: 2020/8/8 13:40
 */
public interface RoleDao {
    /**
     * 根据用户id查询角色数据
     * @param userId
     * @return
     */
    Set<Role> findRoleByUserId(Integer userId);
}
