package com.zizhou.service;

import com.zizhou.entity.PageResult;
import com.zizhou.pojo.CheckItem;

import java.util.List;

/**
 * 检查服务接口
 */
public interface CheckItemService {

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询检查项数据
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据id删除检查项
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id编辑检查项数据
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 根据id查询检查项对象
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
