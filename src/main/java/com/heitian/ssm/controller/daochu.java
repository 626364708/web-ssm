package com.heitian.ssm.controller;


import com.heitian.ssm.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Writer;
import java.util.Map;
/**
 * Created by buj on 2017/7/10.
 */
@Controller
@RequestMapping("global")
public class daochu {


    @Resource
    private UserService userService;

    @RequestMapping("export")
    public void toExcel(HttpServletRequest request,
                        HttpServletResponse response, HttpSession session) {
        String lx = request.getParameter("lx");
        String configName = lx + ".xml"; //找到 模板
//        String title = Constant.lxMap.get(lx);
        String title ="lx";
//        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateStr = dateformat.format(System.currentTimeMillis());
//        String filename = title + dateStr + ".xls";
        String filename = title + ".xls";
//        String xmlLocation = request.getSession().getServletContext().getRealPath(Constant.EXPORT_MODEL_PATH);
        String xmlLocation = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/template/");

        Writer writer = null;
        File _tmp = new File(xmlLocation);
        response.setContentType("text/html;charset=UTF-8");
        try {
            // 设置返回的类型
            response.setContentType("application/vnd.ms-excel");
            filename = new String(filename.getBytes("gb2312"), "ISO8859-1");
            // 设置消息头(文件名)
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            writer = response.getWriter();
            Map<String, Object> map = (Map<String, Object>) session.getAttribute("export_" + lx);
            if (map != null && _tmp.exists()) {

                // 创建配置实例,设置模板加载目录
                Configuration configuration = new Configuration();
                configuration.setDirectoryForTemplateLoading(_tmp);
                // 创建模板
                Template template = configuration.getTemplate(configName, "utf-8");
                // 将数据模型和模板进行合并,并将合并后的文件输出到流中
                template.process(map, writer);
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
