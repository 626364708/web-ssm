package com.heitian.ssm.controller;

import com.heitian.ssm.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by buj on 2017/7/26.
 */
@RequestMapping(value = "/fileup")
@Controller
public class Fileupload {

    @RequestMapping(value = "/test")
    public String Testfileupload(HttpServletRequest request) throws Exception {
        //方法1
        // 转化request
        MultipartHttpServletRequest rm = (MultipartHttpServletRequest) request;
        // 获得文件
        CommonsMultipartFile pic = (CommonsMultipartFile) rm.getFile("pic");
        // 获得文件的字节数组
        byte[] bytes = pic.getBytes();
        // 获得文件的字节数组
        String fileName = "";
        SimpleDateFormat format =  new SimpleDateFormat("yyyyMMddHHmmssSSS");
        fileName = format.format(new Date());
        Random random = new Random();//生成随机的数字int 內型
        for(int i = 0; i < 3; i++){
            fileName = fileName + random.nextInt(9);
        }

        String origFileName = pic.getOriginalFilename();// 获取原有的文件名
        String suffix = origFileName.substring(origFileName.lastIndexOf("."));//得到文件类型
        String path = request.getSession().getServletContext().getRealPath("/");//路径
        OutputStream out = new FileOutputStream(new File(path+"/upload/"+fileName+suffix));
        out.write(bytes);
        out.flush();
        out.close();

        return "showUser";
    }



    //方法2
    @RequestMapping(value = "/test2")
    public String addimg(MultipartHttpServletRequest request) {
        MultipartHttpServletRequest rm = (MultipartHttpServletRequest) request;
        List<MultipartFile> list = request.getFiles("pic");
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            try {
                List<Map<String,String>> relist=new ArrayList<Map<String,String>>();
                for (MultipartFile file : list) {
                    Map<String, String> map = new HashMap<String, String>();
                    String name = UUID.randomUUID().toString() + ".jpg";

                    String path = request.getSession().getServletContext().getRealPath("/");
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    map.put("picname", name);
                    map.put("icon", "/upload/" + name);
                    file.transferTo(new File(path + "/" + name));
                    relist.add(map);

                }
//                return new Result(Result.SUCC, relist, "附件上传成功！");
            } catch (Exception e) {
                e.printStackTrace();
//                return new Result(Result.FAILURE, "附件上传失败！");
            }
        }
        return "showUser";
    }
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    //单张图片上传  要在Sping mvc 里添加  mvc:resources location="/upload/" mapping="/upload/**" /> 这句
    private String fildUpload(User users , @RequestParam(value="file",required=false) MultipartFile file,
                              HttpServletRequest request)throws Exception{
        //基本表单
        System.out.println(users.toString());

        //获得物理路径webapp所在路径
        String pathRoot = request.getSession().getServletContext().getRealPath("");
        String path="";
        if(!file.isEmpty()){
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType=file.getContentType();
            //获得文件后缀名称
            String imageName=contentType.substring(contentType.indexOf("/")+1);
            path="/upload/images/"+uuid+"."+imageName;
            file.transferTo(new File(pathRoot+path));//把文件传到 文件里
        }
        System.out.println(path);
        request.setAttribute("imagesPath", path);
        return "detail";
    }


    @RequestMapping(value="/duoupload",method=RequestMethod.POST)
    //多张图片上传
    private String fUpload(User users ,@RequestParam(value="file",required=false) MultipartFile[] file,
                              HttpServletRequest request)throws Exception{
        //基本表单
        System.out.println(users.toString());

        //获得物理路径webapp所在路径
        String pathRoot = request.getSession().getServletContext().getRealPath("");
        String path="";
        List<String> listImagePath=new ArrayList<String>();
        for (MultipartFile mf : file) {
            if(!mf.isEmpty()){
                //生成uuid作为文件名称
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType=mf.getContentType();
                //获得文件后缀名称
                String imageName=contentType.substring(contentType.indexOf("/")+1);
                path="/upload/images/"+uuid+"."+imageName;
                mf.transferTo(new File(pathRoot+path));
                listImagePath.add(path);
            }
        }
        System.out.println(path);
        request.setAttribute("imagesPathList", listImagePath);
        return "detail";
    }









    //因为我的JSP在WEB-INF目录下面，浏览器无法直接访问
    @RequestMapping(value="/forward")
    private String forward(){
        return "showUser";
    }





}
