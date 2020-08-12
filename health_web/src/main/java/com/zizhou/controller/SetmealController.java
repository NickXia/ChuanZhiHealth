package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.constant.RedisConstant;
import com.zizhou.entity.PageResult;
import com.zizhou.entity.QueryPageBean;
import com.zizhou.entity.Result;
import com.zizhou.pojo.Setmeal;
import com.zizhou.service.SetmealService;
import com.zizhou.utils.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam MultipartFile imgFile){
        try {
            //1.获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();

            //2.截取后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            //3.随机生成文件名称+后缀
            String newFileName = UUID.randomUUID().toString() + suffix;

            //4.调用七牛云工具类
            QiNiuUtil.upload2Qiniu(imgFile.getBytes(),newFileName);

            //1.用户选择图片上传后 需要往redis记录数据 key:setmealPicResources value:图片名称
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);

            //图片上传成功
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 新增套餐的方法
     * @param setmeal 套餐对象
     * @return 返回数据
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.add(checkgroupIds,setmeal);
            //2当用户点击确定 记录redis key:setmealPicDbResources value:图片名称
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return 返回数据
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    /**
     * 根据套餐id查询检查组对象
     * @param id 检查组id
     * @return 返回数据
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            Setmeal setmeal= setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id查询出当前套餐关联的检查组的ids
     * @param id 套餐id
     * @return 返回数据
     */
    @RequestMapping(value = "/findCheckGroupIdsBySetmealId",method = RequestMethod.GET)
    public Result findCheckGroupIdsBySetmealId(Integer id){
        try {
            List<Integer> ids = setmealService.findCheckGroupIdsBySetmealId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 修改套餐信息,将套餐关联的检查组ids提交
     * @param checkgroupIds 套餐对象所关联的检查组ids
     * @param setmeal 套餐对象
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(Integer[] checkgroupIds,@RequestBody Setmeal setmeal){
        try {
            setmealService.edit(checkgroupIds,setmeal);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
}
