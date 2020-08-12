package com.zizhou.service;

import com.zizhou.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Description: 预约设置信息接口
 * @Author: NickXia
 * @date: 2020/8/4 8:55
 */
public interface OrderSettingService {
    /**
     * 批量导入预约信息
     * @param orderSettings
     */
    void add(List<OrderSetting> orderSettings);

    /**
     * 根据年月 查询当月预约设置信息
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 根据日期修改可预约人数
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);
}
