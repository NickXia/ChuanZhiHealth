package com.zizhou.dao;

import com.github.pagehelper.Page;
import com.zizhou.pojo.CheckGroup;
import com.zizhou.pojo.CheckItem;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 保存检查组和中间表数据
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 检查组分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据检查组id查询检查项对象
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询检查项ids
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 根据检查组id更新检查组表
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组id删除中间表已经关联的数据
     * @param groupId
     */
    void deleteAssociation(Integer groupId);
    void deletetSetmealByGroupId(Integer id);

    /**
     * 根据检查组id删除检查组对象
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();


}
