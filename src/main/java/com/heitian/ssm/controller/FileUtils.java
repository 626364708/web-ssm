package com.heitian.ssm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by buj on 2017/7/21.
 */
public class FileUtils {
//http://blog.csdn.net/qq_22063697/article/details/52137369 参考
        public  void  Flietest(String txt) throws IOException {
            File file = new File(txt);
            //传进来的可能不是一个目录
            if(!file.isDirectory()) {   //是否是目录
                throw new IOException(file+"不是目录"); //手动抛出异常
            }
            //传进来的可能是一个错误的路径
            if(file==null){
                throw new IOException("路径不存在");
            }
            File[] files = file.listFiles();
            for (File f : files) {
                //有可能是一个多级目录，递归调用
                if (f.isDirectory()) {
                    Flietest(f.getAbsolutePath());
                    //是文件就直接输出该文件的绝对路径
                }else {
                    System.out.println(f.getAbsolutePath());
                }
            }
        }

    public static void main(String[] args) throws IOException  {

        FileUtils fileUtils = new FileUtils();
        fileUtils.Flietest("D:\\test");  //读取D 盘下test文件下的 文件名

        FileInputStream fileInputStream = new FileInputStream("D:\\test\\2\\fs.txt"); //读文件里的内容
//        int a;
//        while ((a=fileInputStream.read())!=-1){
//            System.out.println((char) a);
//        }

        FileOutputStream fos = new FileOutputStream("22.txt"); //将内容写入文件里
//       String d="放松放松咖啡师傅师傅师傅师傅";
//        byte[] bytes = d.getBytes();
//        fos.write(bytes);


//        int b;
//        while ((b = System.in.read()) != -1) { //获得控制台的输出语句
//            fos.write(b);
//        }

        byte[] b = new byte[1024];
        while((fileInputStream.read(b)) != -1){// 将 D:\test\2\fs.txt 里的内容写入 22.txt 里
            fos.write(b);
        }


        fos.close();     //关闭
        fos.flush();  //清空缓存
        fileInputStream.close();  //关闭












    }




































}
