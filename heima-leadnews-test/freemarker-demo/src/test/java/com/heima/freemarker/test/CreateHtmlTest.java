package com.heima.freemarker.test;

import com.heima.freemarker.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author mianbao
 */
@SpringBootTest
public class CreateHtmlTest {

    @Autowired
    private Configuration configuration;

    @Test
    public void createHtml() throws Exception {
        //读取模板
        Template template = configuration.getTemplate("func.ftl");
        //填充数据
        HashMap<String, Object> data = getData();
        //导出html
        template.process(data, new FileWriter("/Users/mianbao/Documents/freemarker/func.html"));
    }


    public HashMap<String, Object> getData() {

        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        list.add("456");
        map.put("stus", list);
        Date date = new Date();
        map.put("today", date);
        map.put("point", 1234567);
        return map;

    }

}
