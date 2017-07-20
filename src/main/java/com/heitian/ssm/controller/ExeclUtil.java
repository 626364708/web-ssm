package com.heitian.ssm.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ExeclUtil {

    public static Map<String, List<int[]>> rangeAddressMap = null;

    /**
     * @param name     execl的名称 name==null ? new Date(): name
     * @param sheets   sheet名称  is not null
     * @param titles   第一行为表头 不需要表头 titles=null  Map<sheet,title >
     * @param values   每一列的值   Map<sheet,list>
     * @param response
     */
    public static void  createExecl(String name,
                            String[] sheets,
                            Map<String, String[]> titles,
                            Map<String, List<String[]>> values,
                            HttpServletResponse response) {
        //输出Excel文件
        OutputStream output = null;
        try {
            HSSFWorkbook wb=createWorkbook(sheets,titles,values);
           /* //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));*/

            response.setContentType("application/vnd.ms-excel");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            if (StringUtils.isBlank(name)) {
                name = sdf.format(new Date());
            }else{
                name += sdf.format(new Date());
            }
            //解析中文文件名称
            String fileName = new String(name.getBytes("GB2312"), "ISO_8859_1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (Exception e) {
            System.out.println("=======execl导出失败==========" + e.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            rangeAddressMap=null;
        }
    }

    public static HSSFWorkbook createWorkbook(String[] sheets, Map<String, String[]> titles,
                                Map<String, List<String[]>> values){
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        for (String s : sheets) {
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet(s);
            if (titles != null) {
                execlAddTitle(sheet, titles.get(s), values.get(s));
            }else{
                execlAddTitle(sheet, null, values.get(s));
            }
            //合并单元格
            rangeCell(rangeAddressMap.get(s), sheet);
        }
        return wb;
    }

    public static void rangeCell(List<int[]> rangeAddressList, HSSFSheet sheet){
        if (null != rangeAddressList && rangeAddressList.size() != 0){
            for (int[] rd : rangeAddressList) {
                sheet.addMergedRegion(new CellRangeAddress(rd[0], rd[1], rd[2], rd[3]));
            }
        }
    }

    private static void execlAddTitle(HSSFSheet sheet, String[] titles, List<String[]> values) {
        if (titles != null) {
            //添加title
            execlAddCell(sheet,0,titles);
            for(int j=0;j<values.size();j++){
                String[] value=values.get(j);
                execlAddCell(sheet,j+1,value);
            }
        }else{
            for(int j=0;j<values.size();j++){
                String[] value=values.get(j);
                execlAddCell(sheet,j,value);
            }
        }
    }

    private static void execlAddCell(HSSFSheet sheet,int rownum, String[] values){
        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row_title = sheet.createRow(rownum);
        for (int i = 0; i < values.length; i++) {
            //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            HSSFCell cell = row_title.createCell(i);
            //设置单元格内容
            cell.setCellValue(StringUtils.isBlank(values[i])?"":values[i]);
        }
    }

    public static void main(String[] args) {

    }
}
