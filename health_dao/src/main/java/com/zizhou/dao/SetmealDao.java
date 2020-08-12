package com.zizhou.dao;

import com.github.pagehelper.Page;
import com.zizhou.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Description: 套餐持久层dao接口
 * @Author: NickXia
 * @date: 2020/8/2 14:29
 */
public interface SetmealDao {

    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    //void setCheckGroupId(Integer checkgroupId);
    /**
     * 新增或编辑套餐的公共方法
     * @param map
     */
    void setSetmealAndCheckGroup(Map<String, Integer> map);
    //void setCheckGroupId(Integer checkgroupId);

    /**
     * 套餐分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);

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
     * 查询套餐数据
     * @param setmealId
     */
    void deleteAssociation(Integer setmealId);

    /**
     * 根据套餐id更新检查组表
     * @param setmeal
     */
    void edit(Setmeal setmeal);

    /**
     * 查询所有套餐数据
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐名称和预约数量
     * @return
     */
    List<Map> findSetmealCount();

    /**
     * 热门套餐数据
     * @return
     */
    List<Map> findHotSetmeal();


}
