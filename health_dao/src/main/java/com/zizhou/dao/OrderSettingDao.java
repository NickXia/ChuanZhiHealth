package com.zizhou.dao;

import com.zizhou.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 预约设置信息
 * @Author: NickXia
 * @date: 2020/8/4 9:08
 */
public interface OrderSettingDao {
    /**
     * 根据预约日期查询是否已设置预约
     * @param orderDate
     * @return
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 根据预约日期来更新预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 预约设置保存
     * @param orderSetting
     */
    void save(OrderSetting orderSetting);

    /**
     * 根据年月 查询当月预约设置信息
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    /**
     * 根据预约日期查询当前的日期是否可以预约
     * @param newOrderDate
     * @return
     */
    OrderSetting findByOrderDate(Date newOrderDate);

    /**
     * 根据预约日期来编辑
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
