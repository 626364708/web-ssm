package com.heitian.ssm.controller;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by buj on 2017/7/3.
 */
public class Excel_two {

    public void createXls() throws Exception{
        //声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明表
        HSSFSheet sheet = wb.createSheet("第一个表");
        //声明行
        HSSFRow row = sheet.createRow(0);
        //声明列
        HSSFCell cel = row.createCell(0);
        //写入数据
        cel.setCellValue("你也好");

        FileOutputStream fileOut = new FileOutputStream("d:/a/b.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void main(String[] args) throws ParseException {

        Date d1 = new SimpleDateFormat("yyyy-MM").parse("2015-6");//定义起始日期

        Date d2 = new SimpleDateFormat("yyyy-MM").parse("2016-5");//定义结束日期

        Calendar dd = Calendar.getInstance();//定义日期实例

        dd.setTime(d1);//设置日期起始时间

        while (dd.getTime().before(d2)) {//判断是否到结束日期

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            String str = sdf.format(dd.getTime());

            System.out.println(str);//输出日期结果

            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
    }}