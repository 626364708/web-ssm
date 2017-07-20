package com.heitian.ssm.controller;

import com.heitian.ssm.model.MotionRecord;
import com.heitian.ssm.service.UserService;
import org.apache.poi.hssf.usermodel.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by buj on 2017/7/3.
 */
public class Excel {
    @Resource
    private UserService userService;

        private File createExcel(File file){

                if (!file.exists()){ //判断路径是否存在
                    file.mkdirs();
                }
            File filenew = new File(file.getAbsoluteFile() + "V+积分团队排名 .xls");
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("table");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            //创建一个居中格式
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("UserID");
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue("time");
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);
            cell.setCellValue("state");
            cell.setCellStyle(style);

            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
     //       List<User> list = userService.getAllUser();
            List list = null;
            try {
                list = userService.getAllUser();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            for (int i = 0; i < list.size(); i++)
            {
                row = sheet.createRow((int) i + 1);
                MotionRecord stu = (MotionRecord) list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue((double) stu.getUserId());
                row.createCell((short) 1).setCellValue((double)stu.getTime());
                row.createCell((short) 2).setCellValue((double) stu.getState());

            }

            // 第六步，将文件存到指定位置
            try{
                FileOutputStream fout = new FileOutputStream(filenew);
                wb.write(fout);
                fout.close();
            }catch (Exception e){
                e.printStackTrace();
            }
                return  filenew;
        }


    public static void main(String[] args) {

    }


}


