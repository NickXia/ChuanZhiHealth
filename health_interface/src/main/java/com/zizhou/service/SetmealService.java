package com.zizhou.service;

import com.zizhou.entity.PageResult;
import com.zizhou.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Description: 套餐服务接口
 * @Author: NickXia
 * @date: 2020/8/2 14:16
 */
public interface SetmealService {
    /**
     * 新增套餐
     * @param checkgroupIds
     * @param setmeal
     */
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据套餐id查询检查组对象
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 根据套餐id查询出当前套餐关联的检查组的ids
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 修改套餐信息,将套餐关联的检查组ids提交
     * @param checkgroupIds
     * @param setmeal
     */
    void edit(Integer[] checkgroupIds, Setmeal setmeal);

    /**
     * 查询所有套餐数据
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 获取套餐名称和预约数量
     * @return
     */
    List<Map> findSetmealCount();
}
