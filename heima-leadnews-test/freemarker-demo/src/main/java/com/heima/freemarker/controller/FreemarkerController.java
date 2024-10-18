package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public String list(Model model){
        ArrayList<Student> stus = new ArrayList<>();
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
        model.addAttribute("stus",stus);

        //Map
        Map<String, Object> map = new HashMap<>();
        map.put("stu1",s1);
        map.put("stu2",s2);
        model.addAttribute("stuMap",map);
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name","张三");
        Student student = new Student();
        student.setAge(12);
        student.setName("学生姓名");
        model.addAttribute("stu",student);
        return "basic";
    }

}
