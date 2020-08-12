package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zizhou.dao.OrderSettingDao;
import com.zizhou.pojo.OrderSetting;
import com.zizhou.service.OrderSettingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 预约设置信息接口实现类
 * @Author: NickXia
 * @date: 2020/8/4 8:57
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约信息
     * @param orderSettings
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        if(orderSettings != null && orderSettings.size() > 0){
            //1.遍历List<ordersetting> list
            for (OrderSetting orderSetting : orderSettings) {
                orderSettingByOrderDate(orderSetting);
            }
        }
    }

    /**
     * 根据年月 查询当月预约设置信息
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //select * from t_ordersetting where orderdate between '2020-08-01' and '2020-08-31'
        String begin = date + "-01";//起始日期2020-08-01
        String end = date + "-31";//结束日期2020-08-31

        //用Map形式来存放
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //根据年月查询 预约数据
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByMonth(map);

        List<Map> mapList = new ArrayList<>();
        //将List<OrderSetting> 转 list<map>返回
        if(orderSettings != null && orderSettings.size() > 0){
            for (OrderSetting orderSetting : orderSettings) {
                //单个预约设置对象   { date: 1, number: 120, reservations: 1 }
                Map<String,Object> rsMap = new HashMap<>();
                rsMap.put("date",orderSetting.getOrderDate().getDate());//获取预约日期
                rsMap.put("number",orderSetting.getNumber());//获取预约人数
                rsMap.put("reservations",orderSetting.getReservations());//获取已预约人数
                mapList.add(rsMap);
            }
        }
        return mapList;
    }

    /**
     * 根据日期修改可预约人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        orderSettingByOrderDate(orderSetting);
    }

    /**
     * 批量预约设置和单个预约设置方法抽取
     * @param orderSetting
     */
    private void orderSettingByOrderDate(OrderSetting orderSetting) {
        //2.判断当前的日期是否已经被设置过
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //被设置过就更新设置信息
            //根据预约日期 来更新预约人数 update  t_ordersetting set number = xxx where orderDate = 'xxx'
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //没有设置过就进行保存
            orderSettingDao.save(orderSetting);
        }
    }
}
