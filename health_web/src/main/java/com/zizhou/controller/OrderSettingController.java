package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.entity.PageResult;
import com.zizhou.entity.QueryPageBean;
import com.zizhou.entity.Result;
import com.zizhou.pojo.CheckGroup;
import com.zizhou.pojo.OrderSetting;
import com.zizhou.service.CheckGroupService;
import com.zizhou.service.OrderSettingService;
import com.zizhou.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 批量导入预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * Excel文件上传,并将文件内容上传到数据库
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile excelFile){
        try {
            System.out.println("原始文件名:"+excelFile.getOriginalFilename());
            //1.使用POI解析文件,得到List<String[]> list
            List<String[]> list = POIUtils.readExcel(excelFile);
            //2.把List<String[]> list转成List<ordersetting> list
            if (list != null && list.size() > 0){
                List<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] strs : list) {
                    OrderSetting orderSetting = new OrderSetting();//每一行数据
                    orderSetting.setOrderDate(new Date(strs[0]));//预约日期设置
                    orderSetting.setNumber(Integer.parseInt(strs[1]));//预约数量设置
                    orderSettings.add(orderSetting);//把每一行数据放到List集合中
                }
                //3.调用业务,进行保存
                orderSettingService.add(orderSettings);
            }

            //返回文件名
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据年月 查询当月预约设置信息
     */
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.GET)
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> listMap = orderSettingService.getOrderSettingByMonth(date);

            //获取预约设置数据成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据日期修改可预约人数
     */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            //获取预约设置数据成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
