package com.zizhou.dao;

import com.github.pagehelper.Page;
import com.zizhou.pojo.CheckItem;

import java.util.List;

/**
 * 持久层dao接口
 */
public interface CheckItemDao {
    /**
     * 新增检查项
     * @param checkItem 检查项对象
     */
    void add(CheckItem checkItem);

    /**
     * 根据条件分页查询检查项数据
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 先查询中间表t_checkgroup_checkitem是否关联检查组,有关联就提示异常
     * @param checkItemId
     * @return
     */
    int findCountByCheckItemId(Integer checkItemId);

    /**
     * 未关联检查组,根据检查项id直接删除
     * @param checkItemId
     */
    void deleteById(Integer checkItemId);

    /**
     * 根据id编辑检查项对象
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 根据id查询检查项
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

}
