package com.heima.freemarker.test;


import com.heima.freemarker.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.util.*;

@SpringBootTest
public class FreemarkerTest {

    @Resource
    private Configuration configuration;

    @Test
    public void generatePage() throws Exception{
        Template template = configuration.getTemplate("func.ftl");
        Map<String, Object> data = getData();
        template.process(data,new FileWriter("D:\\F\\黑马头条-课堂\\02-自媒体端开发\\demo.html"));
    }

    public Map<String,Object> getData(){
        HashMap<String, Object> map = new HashMap<>();
        List<Student> stus = new ArrayList<>();
        Student stu1 = new Student();
        stu1.setName("一号");
        stu1.setAge(18);
        stu1.setMoney(159F);
        Student stu2 = new Student();
        stu2.setName("二号");
        stu2.setAge(24);
        stu2.setMoney(111F);
        stus.add(stu1);
        stus.add(stu2);
        map.put("stus",stus);

        map.put("today",new Date());
        map.put("point",1234567.89);
        return map;
    }
}
