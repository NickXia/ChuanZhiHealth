package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.constant.RedisMessageConstant;
import com.zizhou.entity.Result;
import com.zizhou.pojo.Member;
import com.zizhou.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 手机号快速登录
 * @Author: NickXia
 * @date: 2020/8/7 11:29
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 用户登录
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        //1. 获得用户输入的信息(Map)
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        //2. 取出redis里面的验证码和用户输入的验证码进行校验
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
        if (StringUtils.isEmpty(redisCode) || StringUtils.isEmpty(validateCode) || !redisCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //3. 如果校验通过
        Member member = memberService.findByTelephone(telephone);
        // - 判断是否是会员,不是会员,自动注册为会员
        if (member == null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        // - 保存用户的登录状态(session)
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);//一个月有效
        response.addCookie(cookie);

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
