package com.zizhou.test;

import com.aliyuncs.exceptions.ClientException;
import com.zizhou.utils.SMSUtils;

/**
 * @Description: 阿里云短信测试
 * @Author: NickXia
 * @date: 2020/8/5 16:43
 */
public class ALiYunTest {
    public static void main(String[] args) throws ClientException {
        SMSUtils.sendShortMessage("SMS_198916605","18916612084","668899");
    }
}
