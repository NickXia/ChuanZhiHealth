package com.zizhou.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description: POI测试类
 * @Author: NickXia
 * @date: 2020/8/3 22:18
 */
public class ExcelTest {
    /**
     * 方式一:从Excel读取数据
     * @throws Exception
     */
    @Test
    public void fun01() throws Exception {
        //1 创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("F:\\JavaSE\\Nickcode\\ChuanZhiHealth\\ChuanZhiHealth_day05\\test\\ExcelTest.xlsx");

        //2 获得工作表对象
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        //3 遍历工作表 获得行对象
        for (Row rows : sheetAt) {
            //4 遍历行对象 获得列对象
            for (Cell cell : rows) {
                //5 获得列里面的内容
                System.out.println(cell.getStringCellValue());
            }
        }
        //6.关闭
        workbook.close();
    }

    /**
     * 方式二:从Excel读取数据
     * @throws Exception
     */
    @Test
    public void fun02() throws Exception {
        //1 创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("F:\\JavaSE\\Nickcode\\ChuanZhiHealth\\ChuanZhiHealth_day05\\test\\ExcelTest.xlsx");

        //2 获得工作表对象
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        //3.获取最后一个行号,从0开始
        int lastRowNum = sheetAt.getLastRowNum();

        //4.遍历工作表 获得行对象
        for (int i = 0; i <= lastRowNum; i++) {
            //根据i获取每一行对象
            XSSFRow row = sheetAt.getRow(i);
            //获取最后一列的列号
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                //循环每一列数据
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }

        //关闭资源
        workbook.close();
    }

    /**
     *  向Excel文件写入数据
     */
    @Test
    public void fun03() throws Exception {
        //1.创建工作簿对象 空Excel对象
        XSSFWorkbook workbook = new XSSFWorkbook();

        //2.创建工作表对象
        XSSFSheet xssfSheet = workbook.createSheet("新人登记信息");

        //3.创建标题行
        XSSFRow titleRow = xssfSheet.createRow(0);
        //4.创建列(格子)对象, 设置内容
        titleRow.createCell(0).setCellValue("姓名");
        titleRow.createCell(1).setCellValue("年龄");
        titleRow.createCell(2).setCellValue("登记时间");

        //创建数据行
        XSSFRow dataRow = xssfSheet.createRow(1);
        dataRow.createCell(0).setCellValue("夏子洲");
        dataRow.createCell(1).setCellValue("18");
        dataRow.createCell(2).setCellValue("2022-05-20");

        XSSFRow dataRow02 = xssfSheet.createRow(2);
        dataRow02.createCell(0).setCellValue("余咪咪");
        dataRow02.createCell(1).setCellValue("18");
        dataRow02.createCell(2).setCellValue("2022-05-20");

        //5.通过流写入到磁盘
        OutputStream outputStream = new FileOutputStream(new File("F:\\JavaSE\\Nickcode\\ChuanZhiHealth\\ChuanZhiHealth_day05\\test\\marriage.xlsx"));
        workbook.write(outputStream);
        outputStream.flush();//刷新
        outputStream.close();//输出流关闭
        workbook.close();//关闭
    }
}
