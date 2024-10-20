package com.heima.freemarker.test;


import com.heima.freemarker.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.util.*;

@SpringBootTest
public class FreemarkerTest {

    @Autowired
    private Configuration configuration;


    @Test
    public void genertePage() throws Exception {

        Template template = configuration.getTemplate("hello.ftl");

        Map<String,Object> dataMap = getData();
        template.process(dataMap,new FileWriter("E:\\hello.html"));

    }

    private Map<String, Object> getData() {
        HashMap<String, Object> map = new HashMap<>();
        List<Student> stus = new ArrayList<>();
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

        map.put("today",new Date());
        map.put("point", 1235467.80);
        return null;
    }

}
