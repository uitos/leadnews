package com.heima.freemarker.test;

import com.heima.freemarker.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.io.FileWriter;
import java.util.*;

@SpringBootTest
public class FreemarkerTest {

    @Autowired
    private Configuration configuration;

    @Test
    public void generatePage() throws Exception {
        //1.加载模板
        Template template = configuration.getTemplate("func.ftl");
        //2.准备数据
        Map<String, Object> dateMap = getDate();
        //3.导出页面
        template.process(dateMap, new FileWriter("D:\\WFW\\demo.html"));
    }

    public Map<String, Object> getDate() {
        Map<String, Object> map = new HashMap<>();
        List<Student> stus = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("小红");
        s1.setAge(18);
        s1.setMoney(1000F);
        Student s2 = new Student();
        s2.setName("小蓝");
        s2.setAge(54);
        s2.setMoney(556680F);
        stus.add(s1);
        stus.add(s2);
        map.put("stus", stus);

        map.put("today", new Date());
        map.put("point", 232435.45);
        return map;
    }
}
