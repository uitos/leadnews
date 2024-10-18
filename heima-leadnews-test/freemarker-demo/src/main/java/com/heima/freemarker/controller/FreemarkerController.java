package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<Student> stus = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("小红");
        s1.setAge(18);
        s1.setMoney(1000.86f);
        Student s2 = new Student();
        s2.setName("小蓝");
        s2.setAge(20);
        s2.setMoney(200.1f);
        stus.add(s1);
        stus.add(s2);
        model.addAttribute("stus",stus);

        Map<String, Object> StuMap = new HashMap<>();
        StuMap.put("stu1", s1);
        StuMap.put("stu2", s2);
        model.addAttribute("stuMap",StuMap );
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name","张三" );
        Student student = new Student();
        student.setName("李四");
        student.setAge(18);
        model.addAttribute("stu",student);
        return "basic";
    }

}
