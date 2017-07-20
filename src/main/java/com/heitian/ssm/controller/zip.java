package com.heitian.ssm.controller;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.IOException;
/**
 * Created by buj on 2017/7/10.
 */
public class zip {

//压缩成ZIP 包  导包
//    <dependency>
//    <groupId>commons-codec</groupId>
//    <artifactId>commons-codec</artifactId>
//    <version>1.9</version>
//    </dependency>


        public static void zip(String src, String des) throws IOException {
            File srcFile = new File(src);// 源文件
            File desFile = new File(des);// 目标zip文件
            Project project = new Project();
            Zip zip = new Zip();
            zip.setProject(project);
            zip.setDestFile(desFile);
            FileSet fileSet = new FileSet();
            fileSet.setProject(project);
            if (srcFile.isFile()) {
                fileSet.setFile(srcFile);
            } else if (srcFile.isDirectory()) {
                fileSet.setDir(srcFile);
            }
            // fileSet.setIncludes("**/*.java"); //包含哪些文件或文件夹
            // eg:zip.setIncludes("*.java")
            // fileSet.setExcludes(...); //排除哪些文件或文件夹
            zip.addFileset(fileSet);
            zip.execute();

        }

    public static void main(String[] args) {
        try {
            zip("D://test", "D://test1.zip");// 压缩整个文件来  ;
           zip("D://test.xlsx", "D://test2.zip");// 压缩单个文件
        }catch (IOException e){
            e.printStackTrace();
        }



    }


    }

