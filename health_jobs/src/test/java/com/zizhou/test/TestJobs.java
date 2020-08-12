package com.zizhou.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description: 加载applicationContext-job.xml 启动Spring框架
 * @Author: NickXia
 * @date: 2020/8/2 17:23
 */
public class TestJobs {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }
}
