package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zizhou.dao.CheckGroupDao;
import com.zizhou.entity.PageResult;
import com.zizhou.pojo.CheckGroup;
import com.zizhou.pojo.CheckItem;
import com.zizhou.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    /**
     * 新增检查项
     * @param checkGroup
     * @param checkitemIds
     */
    /**
     * 新增检查组
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds,CheckGroup checkGroup) {
        //1.保存检查组
        checkGroupDao.add(checkGroup);
        //获取检查组id
        Integer groupId = checkGroup.getId();
        //2.保存检查组和检查项中间表  检查项id 检查组id(保存检查组后 select LAST_INSERT_ID())
        setCheckGroupAndCheckItem(groupId,checkitemIds);
    }

    /**
     * 保存检查组和检查项中间表：
     * 为了更新检查组的时候 能代码公用 提取一个方法
     */
    public void setCheckGroupAndCheckItem(Integer groupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                //将两个参数放入map，为后续配置文件使用map方便
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 检查组分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //PageHelper分页查询 PageHelper设置分页参数 紧跟着的第二行代码一定要进行分页的语句代码
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> checkItemPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }

    /**
     * 根据检查组id查询检查项对象
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查询检查项ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
      return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组信息,将检查组关联的检查项提交
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //1.根据检查组id更新检查组表
        checkGroupDao.edit(checkGroup);

        //2.根据检查组id删除中间表已经关联的数据
        Integer groupId = checkGroup.getId();
        checkGroupDao.deleteAssociation(groupId);

        //3.重新建立检查组和检查项的关联关系
        setCheckGroupAndCheckItem(groupId,checkitemIds);
    }

    /**
     * 根据检查组id删除检查项对象
     * @param id 检查组id
     * @return 返回数据
     */
    @Override
    public void deleteById(Integer id) {
        //根据检查组id删除中间表数据
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deletetSetmealByGroupId(id);

        //根据检查组id删除检查组对象
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询所有检查组信息
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> list = checkGroupDao.findAll();
        return list;
    }

}
