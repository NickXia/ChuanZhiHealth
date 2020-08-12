package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zizhou.dao.MemberDao;
import com.zizhou.pojo.Member;
import com.zizhou.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 会员服务接口实现类
 * @Author: NickXia
 * @date: 2020/8/7 11:56
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 注册新会员
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 根据月份查询   会员数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> findCountByCurrentMonth(List<String> months) {
        //定义一个集合返回统计数量
        List<Integer> rsCount = new ArrayList<>();
        for (String month : months) {
            month = month + "-31";
            Integer count = memberDao.findMemberCountBeforeDate(month);
            rsCount.add(count);
        }
        return rsCount;
    }
}
