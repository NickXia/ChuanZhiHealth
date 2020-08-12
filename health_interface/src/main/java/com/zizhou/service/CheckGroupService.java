package com.zizhou.service;

import com.zizhou.entity.PageResult;
import com.zizhou.pojo.CheckGroup;

import java.util.List;

/**
 * 检查服务接口
 */
public interface CheckGroupService {

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 检查组分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

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
     * 修改检查组信息,将检查组关联的检查项提交
     * @param checkitemIds
     * @param checkGroup
     */
    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 根据检查组id删除检查项对象
     * @param id 检查组id
     * @return 返回数据
     */
    void deleteById(Integer id);

    /**
     * 查询所有检查组信息
     * @return
     */
    List<CheckGroup> findAll();

}
