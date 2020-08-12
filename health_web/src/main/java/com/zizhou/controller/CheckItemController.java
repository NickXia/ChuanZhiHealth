package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.entity.PageResult;
import com.zizhou.entity.QueryPageBean;
import com.zizhou.entity.Result;
import com.zizhou.pojo.CheckItem;
import com.zizhou.service.CheckItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项管理
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    //实例日志对象,记录输入和输出
    private Logger logger = LoggerFactory.getLogger(CheckItemController.class);

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项的方法
     * @param checkItem 检查项对象
     * @return 返回数据
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 检查项分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    //@PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 删除检查项
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){
        try {
            checkItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据id查找检查项(回显数据)
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            //日志级别 error（报错）  warn（业务警告） info（输入输出） debug（开发模式） trace(一般情况不用) 输入
            /*int sex = 3;//0 1 2
            if(sex > 2){
                logger.warn("非法参数");
            }*/
            logger.info("info检查项id="+id);
            logger.error("error检查项id="+id);
            logger.warn("warn检查项id="+id);
            logger.debug("debug检查项id="+id);
            logger.trace("trace检查项id="+id);
            CheckItem checkItem = checkItemService.findById(id);
            System.out.println("===========================");
            logger.info("info检查项="+checkItem.toString());
            logger.error("error检查项="+checkItem.toString());
            logger.warn("warn检查项="+checkItem.toString());
            logger.debug("debug检查项="+checkItem.toString());
            logger.trace("trace检查项="+checkItem.toString());
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据id修改检查项
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有检查项
     * @return
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<CheckItem> checkItems = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
