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
        Template template = configuration.getTemplate("list.ftl");
        //准备数据，是一个Map
        Map data = getData();
        //输出页面
        template.process(data, new FileWriter("D://list.html"));
    }

    @Test  //org.junit.jupiter.api.Test;
    public void generateHtmlTest1() throws Exception {
        //获取模板对象
        Template template = configuration.getTemplate("list.ftl");

        template.process(getData1(),new FileWriter("D://list1.html"));


    }

    private Map getData1() {
        Map data = new HashMap();

        List<Student> students=new ArrayList<>();
        students.add(new Student("张三",12,null,3000F));
        students.add(new Student("李四",18,null,4000F));
        data.put("stus",students);

        Map<String,Student> studentMap=new HashMap();
        studentMap.put("stu1",new Student("王五",33,null,12000F));
        studentMap.put("stu2",new Student("钱六",23,null,12000F));
        data.put("stuMap",studentMap);

        data.put("date1","2021-01-10");
        data.put("date2","2021-01-11");

        data.put("name","张三");



        return data;
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
        data.put("stus",stus);

        //------------------------------------

        //创建Map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        // 3.1 向model中存放Map数据
        data.put("stuMap", stuMap);

        Date now = new Date();
        data.put("date1", now);
        data.put("date2", now);

        data.put("name", "小李");
        return data;
    }

}
