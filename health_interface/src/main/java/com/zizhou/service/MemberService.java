package com.zizhou.service;

import com.zizhou.pojo.Member;

import java.util.List;

/**
 * @Description: 会员服务接口
 * @Author: NickXia
 * @date: 2020/8/7 11:55
 */
public interface MemberService {

    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 注册新会员
     * @param member
     */
    void add(Member member);

    /**
     * 根据月份查询   会员数量
     * @param months
     * @return
     */
    List<Integer> findCountByCurrentMonth(List<String> months);
}
