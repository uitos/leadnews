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
import java.util.Map;

@SpringBootTest
public class FreeMakerTest {

    @Autowired
    Configuration configuration;

    @Test
    public void generatePage() throws Exception {
        //1.加载模版
        Template template = configuration.getTemplate("func.ftl");

        //2.准备数据
        Map<String, Object> dataMap = getData();

        //3.导出页面
        template.process(dataMap, new FileWriter("/Users/aaronlee/Downloads/test.html"));

    }

    private Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<Student> stus = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("小红");
        s1.setAge(18);
        s1.setMoney(120F);
        Student s2 = new Student();
        s2.setName("小蓝");
        s2.setAge(30);
        s2.setMoney(180F);
        stus.add(s1);
        stus.add(s2);
        map.put("stus", stus);

        //这里map相当于model
        map.put("today", new Date());
        map.put("point", 12.89774);
        return map;
    }


}
