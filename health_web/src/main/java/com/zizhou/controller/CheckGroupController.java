package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.entity.PageResult;
import com.zizhou.entity.QueryPageBean;
import com.zizhou.entity.Result;
import com.zizhou.pojo.CheckGroup;
import com.zizhou.pojo.CheckItem;
import com.zizhou.service.CheckGroupService;
import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组管理
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 新增检查组的方法
     * @param checkGroup 检查组对象
     * @return 返回数据
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.add(checkitemIds,checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 检查项分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据检查组id查询检查项对象
     * @param id 检查组id
     * @return 返回数据
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组的id查询出当前检查组关联的检查项的ids
     * @param id 检查组id
     * @return 返回数据
     */
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId",method = RequestMethod.GET)
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> ids = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 修改检查组信息,将检查组关联的检查项提交
     * @param checkitemIds 检查组对象所关联的检查项ids
     * @param checkGroup 检查组对象
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.edit(checkitemIds,checkGroup);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组id删除检查项对象
     * @param id 检查组id
     * @return 返回数据
     */
    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    public Result deleteById(Integer id){
        try {
            checkGroupService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有检查组信息
     * @param
     * @return 返回数据
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
