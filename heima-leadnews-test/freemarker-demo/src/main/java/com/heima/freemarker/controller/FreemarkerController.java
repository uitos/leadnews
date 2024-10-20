package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
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
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        student.setMoney(100.0f);
        student.setBirthday(new Date());
        Student student1 = new Student();
        student1.setName("小红");
        student1.setAge(19);
        student1.setMoney(100.0f);
        student1.setBirthday(new Date());
        students.add(student);
        students.add(student1);
        model.addAttribute("stus", students);
        model.addAttribute("today", new Date());
            model.addAttribute("point",122222222.1111);
        return "func";
    }

    @GetMapping("/list")
    public String list(Model model) {

        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        student.setMoney(100.0f);
        student.setBirthday(new Date());
        Student student1 = new Student();
        student1.setName("小红");
        student1.setAge(19);
        student1.setMoney(100.0f);
        student1.setBirthday(new Date());
        students.add(student);
        students.add(student1);
        model.addAttribute("stus", students);
        HashMap<String, Object> map = new HashMap<>();
        map.put("stu1", student);
        map.put("stu2", student1);
        model.addAttribute("stuMap", map);
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        model.addAttribute("name", "小明");
        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        model.addAttribute("stu", student);
        return "basic";
    }

}
