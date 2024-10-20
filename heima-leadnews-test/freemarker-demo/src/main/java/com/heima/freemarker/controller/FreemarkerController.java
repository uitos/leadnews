package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        ArrayList<Student> stus = new ArrayList<>();
        Student stu1 = new Student();
        stu1.setAge(10);
        stu1.setBirthday(new Date());
        stu1.setName("小红");
        stu1.setMoney(10.0F);
        stus.add(stu1);
        Student stu2 = new Student();
        stu2.setAge(10);
        stu2.setBirthday(new Date());
        stu2.setName("小红");
        stu2.setMoney(10.0F);
        stus.add(stu2);
        model.addAttribute("stus",stus);
        model.addAttribute("today",new Date());
        model.addAttribute("point",12345678.89);

        return "func";
    }

    @GetMapping("/list")
    public String list(Model model){
        ArrayList<Student> stus = new ArrayList<>();
        Student stu1 = new Student();
        stu1.setAge(10);
        stu1.setBirthday(new Date());
        stu1.setName("小红");
        stu1.setMoney(10.0F);
        stus.add(stu1);
        Student stu2 = new Student();
        stu2.setAge(10);
        stu2.setBirthday(new Date());
        stu2.setName("小红");
        stu2.setMoney(10.0F);
        stus.add(stu2);
        model.addAttribute("stus",stus);

        HashMap<String, Object> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        model.addAttribute("stuMap",stuMap);
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name","张三");
        Student student = new Student();
        student.setAge(10);
        student.setBirthday(new Date());
        student.setName("张三2");
        model.addAttribute("stu",student);
        return "basic";
    }

}
