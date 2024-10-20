package com.heima.freemarker.test;

import com.heima.freemarker.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.util.*;

/**
 * @ClassName FreemarkerTest
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/20
 * @Version 1.0
 */
@SpringBootTest
public class FreemarkerTest {

    @Resource
    private Configuration configuration;

    @Test
    public void testFreemarker() throws Exception {
        //加载模板
        Template template = configuration.getTemplate("func.ftl");
        //封装数据
        Map<String, Object> map = getData();
        //导出页面
        template.process(map, new FileWriter("D:\\Temp\\demo.html"));
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        List<Student> stus = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("小明");
        s1.setAge(18);
        s1.setMoney(100.0f);
        Student s2 = new Student();
        s2.setName("小红");
        s2.setAge(20);
        s2.setMoney(200.0f);
        stus.add(s1);
        stus.add(s2);
        map.put("stus", stus);

        map.put("today", new Date());
        map.put("point", 123456789.10);
        return map;
    }

}
