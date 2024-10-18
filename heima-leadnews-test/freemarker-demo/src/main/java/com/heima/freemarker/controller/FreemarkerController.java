package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
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
        return "func";
    }

    @GetMapping("/list")
    public String list(Model model) {
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
        model.addAttribute("stus",stus);

        // Map 
        HashMap<String, Object> map = new HashMap<>();
        map.put("stu1",stu1);
        map.put("stu2",stu2);
        model.addAttribute("stuMap",map);
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        model.addAttribute("name", "留洋");
        Student student = new Student();
        student.setName("文龙");
        student.setAge(23);
        model.addAttribute("stu", student);
        return "basic";
    }

}
