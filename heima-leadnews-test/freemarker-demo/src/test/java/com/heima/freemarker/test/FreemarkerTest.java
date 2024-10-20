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
 * @date 2024-10-20 09:35:03
 */
@SpringBootTest
public class FreemarkerTest {

    @Autowired
    private Configuration configuration;

    @Test
    public void generatePage() throws Exception {
        // 1.加载模板
        Template template = configuration.getTemplate("func.ftl");
        // 2.准备数据
        Map<String, Object> dataMap = getData();
        // 3.导出页面
        template.process(dataMap, new FileWriter("D:\\temp\\demo.html"));
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
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

        map.put("today", new Date());
        map.put("point", 1234567.89);
        return map;
    }


}
