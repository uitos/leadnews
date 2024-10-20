package com.heima.freemarker.test;

import com.heima.freemarker.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-05 17:31:33
 */
@SpringBootTest
public class FreemarkerTest {
    
    @Autowired
    private Configuration configuration;
    
    /**
     * 
     * @throws Exception
     */
    @Test  //org.junit.jupiter.api.Test;
    public void generateHtmlTest() throws Exception {
        //获取模板对象
        Template template = configuration.getTemplate("func.ftl");
        //准备数据，是一个Map
        Map<String, Object> data = getData();
        //输出页面
        template.process(data, new FileWriter("D:\\hello.html"));
    }

    private Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        //------------------------------------
        Student stu1 = new Student();
        stu1.setName("小强");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        //小红对象模型数据
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);

        data.put("stus", stus);

        data.put("today", new Date());
        data.put("point", 1234567.89);
        return data;
    }

}
