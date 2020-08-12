package com.zizhou.dao;

import com.zizhou.pojo.Permission;

import java.util.Set;

/**
 * @Description: 权限dao接口
 * @Author: NickXia
 * @date: 2020/8/8 13:41
 */
public interface PermissionDao {
    /**
     * 根据角色id查询权限数据
     * @param roleId
     * @return
     */
    Set<Permission> findPermissionByRoleId(Integer roleId);
}
