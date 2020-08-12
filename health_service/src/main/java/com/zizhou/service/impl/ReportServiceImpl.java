package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zizhou.dao.MemberDao;
import com.zizhou.dao.OrderDao;
import com.zizhou.dao.SetmealDao;
import com.zizhou.service.MemberService;
import com.zizhou.service.ReportService;
import com.zizhou.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 获取运营数据接口实现类
 * @Author: NickXia
 * @date: 2020/8/10 17:39
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        //获取今天的日期
        String reportDate = DateUtils.parseDate2String(new Date());
        //获取本周第一天的日期
        String thisWeekDayMonday = DateUtils.parseDate2String(DateUtils.getToday());
        //获取本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //今日新增会员数 SELECT COUNT(*) FROM t_member WHERE regTime = '2020-08-10'
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);

        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekDayMonday);

        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekDayMonday);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekDayMonday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐
        List<Map> mapList = setmealDao.findHotSetmeal();

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("reportDate",reportDate);
        rsMap.put("todayNewMember",todayNewMember);
        rsMap.put("totalMember",totalMember);
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",mapList);

        return rsMap;
    }
}
