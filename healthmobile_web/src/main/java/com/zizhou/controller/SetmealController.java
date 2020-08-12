package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.entity.Result;
import com.zizhou.pojo.Setmeal;
import com.zizhou.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐数据
     * @param
     * @return
     */
    @RequestMapping(value = "/getSetmeal", method = RequestMethod.POST)
    public Result getSetmeal(){
        try {
            List<Setmeal> list = setmealService.findAll();
            //图片上传成功
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据id查询套餐里的检查组
     * @param
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
