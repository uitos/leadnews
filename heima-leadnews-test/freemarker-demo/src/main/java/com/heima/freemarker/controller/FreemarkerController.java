package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-05 16:09:31
 */
@Controller
public class FreemarkerController {


    @GetMapping("func")
    public String func(Model model) {
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
        model.addAttribute("stus", stus);

        model.addAttribute("today", new Date());
        model.addAttribute("point", 123456789.10);
        return "func";
    }

    @GetMapping("/list")
    public String list(Model model) {
        Student s1 = new Student();
        s1.setName("小明");
        s1.setAge(18);
        s1.setMoney(100.0f);
        Student s2 = new Student();
        s2.setName("小红");
        s2.setAge(20);
        s2.setMoney(200.0f);
        List<Student> stus = new ArrayList<>();
        stus.add(s1);
        stus.add(s2);
        model.addAttribute("stus", stus);

        Map<String, Object> map = new HashMap<>();
        map.put("stu1", s1);
        map.put("stu2", s2);
        model.addAttribute("stuMap", map);

        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis() + 1000);
        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);
        model.addAttribute("name", "小明");
        model.addAttribute("age", 10);

        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        model.addAttribute("name", "张三");
        Student student = new Student();
        student.setName("李四");
        student.setAge(18);
        model.addAttribute("stu", student);
        return "basic";
    }

}
