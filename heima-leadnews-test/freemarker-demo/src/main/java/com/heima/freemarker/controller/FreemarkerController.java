package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-05 16:09:31
 */
@Controller
public class FreemarkerController {


    @GetMapping("func")
    public String func(Model model) {

        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        list.add("456");
        model.addAttribute("stus", list);
        Date date = new Date();
        model.addAttribute("today", date);
        model.addAttribute("point", 1234567);
        return "func";

    }

    @GetMapping("/list")
    public String list(Model model) {
        Student s1 = new Student();
        s1.setName("小红");
        s1.setAge(18);
        s1.setMoney(20f);
        Student s2 = new Student();
        s2.setName("小蓝");
        s2.setAge(19);
        s2.setMoney(22f);
        ArrayList<Student> stus = new ArrayList<>();
        stus.add(s1);
        stus.add(s2);
        model.addAttribute("stus", stus);

        HashMap<String, Object> stuMap = new HashMap<>(16);
        stuMap.put("stu1", s1);
        stuMap.put("stu2", s2);
        model.addAttribute("stuMap", stuMap);

        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis() + 200);

        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);

        model.addAttribute("age", 32);

        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        model.addAttribute("name", "测试一下");
        Student student = new Student();
        student.setAge(18);
        student.setName("debu");
        model.addAttribute("stu", student);
        return "basic";
    }

}
