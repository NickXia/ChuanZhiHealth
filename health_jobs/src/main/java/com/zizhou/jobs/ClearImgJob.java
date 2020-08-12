package com.zizhou.jobs;

import com.zizhou.constant.RedisConstant;
import com.zizhou.utils.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Description: 定时清理图片
 * @Author: NickXia
 * @date: 2020/8/2 17:53
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        System.out.println("任务运行了....");
        /**
         * 第一步：
         * 1.用户选择图片上传后 需要往redis记录数据 key:setmealPicResources value:图片名称
         * 2.当用户点击确定 记录redis key:setmealPicDbResources value:图片名称
         * 第二步：
         * 3.从redis中将两个集合中的数据相减  传入两个key参数就可以了
         * sdiff:两个相减  srem：删除集合中数据
         * */
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(sdiff != null && sdiff.size() >0){
            for (String pic : sdiff) {
                System.out.println("图片被清理了...");
                //4.清理七牛云
                QiNiuUtil.deleteFileFromQiniu(pic);
                //清理的集合key 和集合中图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
            }
        }

    }
}
