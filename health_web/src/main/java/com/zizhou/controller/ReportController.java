package com.zizhou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zizhou.constant.MessageConstant;
import com.zizhou.entity.Result;
import com.zizhou.service.MemberService;
import com.zizhou.service.ReportService;
import com.zizhou.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 报告控制层
 * @Author: NickXia
 * @date: 2020/8/8 15:52
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    //导入日志对象
    Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员统计折线图
     * @return
     */
    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    public Result getUsername(){
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();

        //1.获取当前时间
        Calendar calendar = Calendar.getInstance();

        //2.往前推12个月
        calendar.add(Calendar.MONTH,-12);

        for (int i = 0; i < 12; i++) {
            //3.每次+1,得到时间存入list
            calendar.add(Calendar.MONTH,1);
            //把时间转成     "2020-08"这样的格式
            Date time = calendar.getTime();
            String nowDate = new SimpleDateFormat("yyyy-MM").format(time);
            months.add(nowDate);
        }

        //sql语句:SELECT * FROM t_member WHERE regTime < '2020-03-31'
        List<Integer> memberCount = memberService.findCountByCurrentMonth(months);
        map.put("months",months);//月份数据
        map.put("memberCount",memberCount);//会员数量
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 套餐预约占比饼形图
     * @return
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    public Result getSetmealReport(){
        /*
        "setmealNames":['入职体检套餐','测试套餐','血常规套餐'],
        "setmealCount":[
            {value:10,name:'入职体检套餐'},
            {value:2,name:'测试套餐'},
            {value:5,name:'血常规套餐'}
        ]*/
        //查询套餐及其预约数量:SELECT ts.`name`, COUNT(o.id) VALUE FROM t_setmeal ts, t_order o WHERE ts.id = o.setmeal_id GROUP BY ts.`name`
        try {
            List<Map> setmealCount = setmealService.findSetmealCount();
            List<String> setmealNames = new ArrayList<>();
            if (setmealCount != null && setmealCount.size() > 0){
                for (Map map : setmealCount) {
                    String name = (String) map.get("name");//获取套餐名称
                    setmealNames.add(name);
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("setmealNames",setmealNames);//套餐名称
            map.put("setmealCount",setmealCount);//套餐名称和套餐数量   页面会根据套餐数量自动计算占比
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 获取运营数据
     * @return
     */
    @RequestMapping(value = "/getBusinessReportData",method = RequestMethod.GET)
    public Result getBusinessReportData(){
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据统计报表
     * @return
     */
    /*@RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();

            //获取模板路径
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            logger.debug("文件路径:" + filePath);
            //1.获取Excel对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));

            //2.为运营数据统计日期赋值
            XSSFSheet sheet = workbook.getSheetAt(0);//标签页
            XSSFRow row = sheet.getRow(2);//获取行
            row.getCell(5).setCellValue((String) rsMap.get("reportDate"));

            Integer todayNewMember = (Integer) rsMap.get("todayNewMember");
            Integer totalMember = (Integer) rsMap.get("totalMember");
            Integer thisWeekNewMember = (Integer) rsMap.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) rsMap.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) rsMap.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) rsMap.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) rsMap.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) rsMap.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) rsMap.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) rsMap.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) rsMap.get("hotSetmeal");

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热门套餐循环
            if(hotSetmeal != null && hotSetmeal.size()>0){
                int rowNum =12;//从12行开始
                for (Map map : hotSetmeal) {
                    //创建行
                    XSSFRow setmealRow = sheet.getRow(rowNum);
                    setmealRow.getCell(4).setCellValue((String)map.get("name"));
                    setmealRow.getCell(5).setCellValue(map.get("setmeal_count").toString());
                    setmealRow.getCell(6).setCellValue(map.get("proportion").toString());
                    setmealRow.getCell(7).setCellValue((String)map.get("remark"));
                    ///写完一行数据后
                    rowNum ++;
                }
            }

            //3.以文件流形式 下载本地磁盘 通过response对象返回文件流
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//以文件附件形式下载 文件名是report.xlsx
            workbook.write(outputStream);

            //4.释放资源
            outputStream.flush();
            outputStream.close();
            workbook.close();
            //return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            //return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }*/

    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> rsMap = reportService.getBusinessReportData();

            //获取模板路径
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template02.xlsx";

            logger.debug("文件路径:" + filePath);
            //1.获取Excel对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));

            XLSTransformer xlsTransformer = new XLSTransformer();
            xlsTransformer.transformWorkbook(workbook,rsMap);

            //3.以文件流形式 下载本地磁盘 通过response对象返回文件流
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//以文件附件形式下载 文件名是report.xlsx
            workbook.write(outputStream);

            //4.释放资源
            outputStream.flush();
            outputStream.close();
            workbook.close();
            //return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            //return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
