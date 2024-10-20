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


@SpringBootTest
public class FreemarkerTest {

    @Autowired
    private Configuration configuration;  //freemarker.template.Configuration;

    @Test
    public void createHtml() throws Exception {
        // 1.加载模版文件
        Template template = configuration.getTemplate("func.ftl");
        // 2.准备数据
        Map data = getData2();
        // 3.输出文件
        template.process(data, new FileWriter("D://func.html"));
    }

    public Map getData2() {
        Map<String, Object> model = new HashMap<>();
        List<Student> stus = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("小明");
        s1.setAge(18);
        s1.setMoney(1000.8f);
        stus.add(s1);
        Student s2 = new Student();
        s2.setName("小红");
        s2.setAge(19);
        s2.setMoney(2000.8f);
        stus.add(s2);
        model.put("stus", stus);
        model.put("today", new Date());
        Double num = 123456789.123;
        model.put("point", num);
        return model;
    }

    /**
     * @throws Exception
     */
    @Test  //org.junit.jupiter.api.Test;
    public void generateHtmlTest() throws Exception {
        //获取模板对象
        Template template = configuration.getTemplate("list.ftl");
        //准备数据，是一个Map
        Map data = getData();
        //输出页面
        template.process(data, new FileWriter("D://list.html"));
    }

    private Map getData() {
        Map data = new HashMap();
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

        //将两个对象模型数据存放到List集合中
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向model中存放List集合数据
        data.put("stus", stus);

        //------------------------------------

        //创建Map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        // 3.1 向model中存放Map数据
        data.put("stuMap", stuMap);

        Date now = new Date();
        data.put("date1", now);
        data.put("date2", now);

        data.put("name", "小李");
        return data;
    }

}
