package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        s1.setMoney(1000.8f);
        stus.add(s1);
        Student s2 = new Student();
        s2.setName("小红");
        s2.setAge(19);
        s2.setMoney(2000.8f);
        stus.add(s2);
        model.addAttribute("stus", stus);
        model.addAttribute("today", new Date());
        Double num = 123456789.123;
        model.addAttribute("point", num);
        return "func";
    }

    @GetMapping("/list")
    public String list(Model model){
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
        model.addAttribute("stus", stus);
        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis()+10000);
        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name", "hello world");
        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        model.addAttribute("stu", student);
        return "basic";
    }

}
