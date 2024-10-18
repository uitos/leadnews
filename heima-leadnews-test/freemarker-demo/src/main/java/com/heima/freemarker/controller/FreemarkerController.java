package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
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
        return "func";
    }

    @GetMapping("/list")
    public String list(Model model){
        Student s1 = new Student();
        s1.setName("小红");
        s1.setAge(18);
        s1.setMoney(1000f);
        Student s2 = new Student();
        s2.setName("小紫");
        s2.setAge(20);
        s2.setMoney(2000f);
        ArrayList<Object> stus = new ArrayList<>();
        stus.add(s1);
        stus.add(s2);
        model.addAttribute("stus",stus);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("stu1",s1);
        hashMap.put("stu2",s2);
        model.addAttribute("stuMap",hashMap);
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name","西门吹雪");
        Student student = new Student();
        student.setName("东方不败");
        student.setAge(18);
        model.addAttribute("stu",student);
        return "basic";
    }

}
