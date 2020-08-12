package com.zizhou.controller;

import com.aliyuncs.exceptions.ClientException;
import com.zizhou.constant.MessageConstant;
import com.zizhou.constant.RedisMessageConstant;
import com.zizhou.entity.Result;
import com.zizhou.utils.SMSUtils;
import com.zizhou.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;

/**
 * @Description: 发送验证码的控制器
 * @Author: NickXia
 * @date: 2020/8/6 12:14
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //体检时发送预约验证码
    @RequestMapping(value = "/send4Order",method = RequestMethod.POST)
    public Result send4Order(String telephone){
        try {
            //生成四位数验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            System.out.println("手机号码：：："+telephone+"验证码：：："+code);
            //使用工具类发送验证码
            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());

            //将生成的验证码缓存到redis
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER  + "_" + telephone,5*60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    //手机号快速登录时发送验证码
    @RequestMapping(value = "/send4Login",method = RequestMethod.POST)
    public Result send4Login(String telephone){
        try {
            //生成四位数验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            System.out.println("验证码:" + code);

            //把验证码存到redis里面(5分钟)
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone,5*60,code.toString());

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
