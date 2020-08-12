package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.zizhou.constant.MessageConstant;
import com.zizhou.constant.RedisMessageConstant;
import com.zizhou.entity.Result;
import com.zizhou.pojo.Order;
import com.zizhou.service.OrderService;
import com.zizhou.utils.SMSUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Description: 体检预约
 * @Author: NickXia
 * @date: 2020/8/6 12:48
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约体检的信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String orderDate = (String) map.get("orderDate");
        String validateCode = (String) map.get("validateCode");

        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);

        if (StringUtils.isEmpty(redisCode) || StringUtils.isEmpty(validateCode) || !redisCode.equals(validateCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

        //logger.error(redisCode);
        //调用业务层代码
        Result result = null;
        try {
            //后台服务和移动端都可以调用service服务 体检预约分为两种预约方式：电话预约 微信预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //预约成功,发送短信通知
        if(result.isFlag()){
            try {
                System.out.println("您已经成功预约传智健康体检，体检时间为: " + orderDate);
                //SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  result;
    }

    /**
     * 展示预约成功后相关信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById(Integer id){
        try {
            Map map = orderService.findById4Detail(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
