package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zizhou.dao.CheckItemDao;
import com.zizhou.entity.PageResult;
import com.zizhou.pojo.CheckItem;
import com.zizhou.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
@Service(interfaceClass=CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //PageHelper分页查询 PageHelper设置分页参数 紧跟着的第二行代码一定要进行分页的语句代码
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }

    /**
     * 删除检查项(不能直接删除,删除之前应该判断是否与检查组有关联)
     * @param checkItemId 检查项id
     */
    @Override
    public void delete(Integer checkItemId) {
        int count = checkItemDao.findCountByCheckItemId(checkItemId);

        //判断是否有关联检查组,有关联就删除该检查组关联的检查项ids
        if(count > 0){
            throw new RuntimeException("已关联检查组,无法删除!");
        }

        //如果未关联检查组,就直接删除
        checkItemDao.deleteById(checkItemId);
    }

    /**
     * 根据id编辑检查项对象
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 根据id查询检查项对象
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> checkItems = checkItemDao.findAll();
        return checkItems;
    }
}
