package com.heitian.ssm.controller;

import com.heitian.ssm.model.User;
import com.heitian.ssm.service.UserService;
import com.heitian.ssm.utils.RequestUtils;
import com.heitian.ssm.utils.impexp4xls.POIExporterImpl;
import com.heitian.ssm.utils.impexp4xls.XLSExporter;
import com.hui.platform.framework.dao.HuiPageCond;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/user")
public class UserController {

    private Logger log = Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;



    public void main(String[] args) throws ParseException {
//        ArrayList<Object> objects = new ArrayList<Object>();
//        HashMap hashMap = new HashMap();
//        hashMap.put("a", 11);
//        hashMap.put("b", 22);
//        hashMap.put("c", 33);
//        hashMap.put("d", 44);
//
//        objects.add(hashMap);
//        System.out.println("args = [" + hashMap.size() + "]");
//        System.out.println("args = [" + objects.size() + "]");

        Date d1 = new SimpleDateFormat("yyyy-MM").parse("2017-1-1");//定义起始日期

        Date d2 = new SimpleDateFormat("yyyy-MM").parse("2017-5-1");//定义结束日期

        Calendar dd = Calendar.getInstance();//定义日期实例

        dd.setTime(d1);//设置日期起始时间

        while(dd.getTime().before(d2)) {//判断是否到结束日期

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            String str = sdf.format(dd.getTime());

            System.out.println(str);//输出日期结果

            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1

        }
    }
    @RequestMapping("index")
    public String list(HttpSession session) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", new ArrayList());
        session.setAttribute("export_TJ", map);

        return "detail";
    }

    @RequestMapping("/showUser")
    @ResponseBody
    public Map<String, Object> showUser(HttpServletRequest request, Model model, HttpSession session) {


        log.info("开始");
        List<Map<String ,Object>> list=userService.getall();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        session.setAttribute("export_TJ", map);
        return  map;

//        List<User> userList = userService.getAllUser();
//        model.addAttribute("userList", userList);
//     return "detail";


    }

    @RequestMapping("/lol")
    public void daochu(HttpServletResponse response, HttpServletRequest request) throws ParseException {
        HuiPageCond pg = RequestUtils.getHuiPage(request);
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");

        List<User> list = userService.getAllUser();


//        List<Map> dataList = new ArrayList<Map>();
//        int i=1;
//        for (User user : list) {
//            Map m = new HashMap<>();
//            m.put("xuhao",i++);
//            m.put("userName",user.getUserName());
//            m.put("age",user.getAge());
//            m.put("userPwd",user.getUserPwd());
//
//            dataList.add(m);
//        }


        OutputStream outStream = null;
        String filename = "V+积分团队排名.xml";
        try {
            String tempfileName = new String(filename.getBytes("GBK"),
                    "ISO8859_1");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment; filename="
                    + new String("V+积分团队排名.xls".getBytes("GBK"), "ISO8859-1"));
            // 获取系统所在服务器本地路径
            String webPath = request.getSession().getServletContext()
                    .getRealPath("/");
            // 创建一个存放下载文件的临时目录
            String tempFile = "tempDownLoad";
            File tempDir = new File(webPath + "/" + tempFile);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            outStream = response.getOutputStream();
            System.out.println(outStream+"===========111111111111========");
            // 处理时间
            Map cusMap = new HashMap();
            StringBuffer log = new StringBuffer("导出");
            StringBuffer titlebuffer = new StringBuffer("团队排名统计表");
            if (StringUtils.hasText(starttime)) {
                log.append(starttime + "到" + endtime + "时间段,");
                titlebuffer.append(starttime + "至" + endtime);
            }
            titlebuffer.append(",0,0");

            XLSExporter exp = new POIExporterImpl();
            exp.exportXLS(outStream, filename, list, cusMap);
            System.out.println(outStream+"============2222222222222====================");
            //log.append(dept.getName() + "团队排名统计表");
            //request.setAttribute(Constant.LOG_RECORD, new SysOperLog("团队排名统计表", ConstantMap.MODUAL_VIntegral, log.toString(), Constant.DAOCHU));
            outStream.flush();  //清空缓存
            outStream.close();  //关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/save")
    public String Save(@ModelAttribute("form") User user, Model model) {
        model.addAttribute("user", user);
        System.out.println("哈哈");
        return "detail";
    }

}