package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zizhou.constant.RedisConstant;
import com.zizhou.dao.SetmealDao;
import com.zizhou.entity.PageResult;
import com.zizhou.pojo.Setmeal;
import com.zizhou.service.CheckGroupService;
import com.zizhou.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:新增套餐的服务接口
 * @Author: NickXia
 * @date: 2020/8/2 14:19
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 新增套餐
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //1.保存套餐组数据
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();

        //2.根据检查组ids循环保存套餐检查组中间表
        setSetmealAndCheckGroup(setmealId,checkgroupIds);

    }

    /**
     * 套餐分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }


    /**
     * 新增或编辑套餐的公共方法
     * @param setmealId
     * @param checkgroupIds
     */
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if(checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                //setmealDao.setCheckGroupId(checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

    /**
     * 根据套餐id查询检查项对象
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 根据套餐id查询出当前套餐关联的检查组的ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐信息,将检查组关联的检查项提交
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    public void edit(Integer[] checkgroupIds,Setmeal setmeal) {
        //1.根据套餐id更新套餐表
        setmealDao.edit(setmeal);

        //2.根据套餐id删除中间表已经关联的数据
        Integer setmealId = setmeal.getId();
        setmealDao.deleteAssociation(setmealId);

        //3.重新建立套餐和检查组的关联关系
        setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }

    /**
     * 查询所有套餐数据
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
       return setmealDao.findAll();
    }

    /**
     * 查询套餐名称和预约数量
     * @return
     */
    @Override
    public List<Map> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
