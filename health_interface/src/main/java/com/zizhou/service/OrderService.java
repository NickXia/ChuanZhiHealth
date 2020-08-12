package com.zizhou.service;

import com.zizhou.entity.Result;

import java.util.Map;

/**
 * @Description: 体检预约接口
 * @Author: NickXia
 * @date: 2020/8/6 13:17
 */
public interface OrderService {
    /**
     * 提交体检预约信息
     * @param map
     * @return
     */
    Result submitOrder(Map map) throws Exception;

    /**
     * 展示预约成功后相关信息
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);
}
