package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
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

        model.addAttribute("today",new Date());
        model.addAttribute("point",1234567.89);
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

        // Date
        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis()+1000);
        model.addAttribute("date1",date1);
        model.addAttribute("date2",date2);

        model.addAttribute("name","文龙");
        model.addAttribute("age",18);

        ArrayList<Object> arr = new ArrayList<>(15);
        System.out.println("arrayList设置固定长度；"+arr.size());

        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        model.addAttribute("name", "留洋");
        Student student = new Student();
        student.setName("文龙");
        student.setAge(18);
        model.addAttribute("stu", student);
        return "basic";
    }

}
