package com.zizhou.service;

import java.util.Map;

/**
 * @Description: 获取运营数据接口
 * @Author: NickXia
 * @date: 2020/8/10 17:37
 */
public interface ReportService {
    /**
     * 获取运营数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws Exception;
}
