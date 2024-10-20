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
    private Configuration configuration;  //freemarker.template.Configuration;
    
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
        template.process(data, new FileWriter("D://temp//demo.html"));
    }

    private Map<String, Object> getData() {
        HashMap<String, Object> map = new HashMap<>();
        Student s1 = new Student();
        s1.setName("小红");
        s1.setAge(18);
        s1.setMoney(1000f);
        Student s2 = new Student();
        s2.setName("小紫");
        s2.setAge(20);
        s2.setMoney(2000f);
        ArrayList<Object> stus = new ArrayList<>();
        stus.add(s1);
        stus.add(s2);
        map.put("stus",stus);
        map.put("today",new Date());
        map.put("point",0.123456789123);
        return map;
    }

}
