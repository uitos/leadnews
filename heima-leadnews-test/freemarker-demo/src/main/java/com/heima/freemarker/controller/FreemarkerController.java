package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("张三",18,new Date(),50F));
        studentList.add(new Student("小红",18,new Date(),500F));
        studentList.add(new Student("小蓝",18,new Date(),5000F));
        studentList.add(new Student("小绿",18,new Date(),50000F));
        model.addAttribute("stus",studentList);

        Date now = new Date();
        model.addAttribute("today",now);
        model.addAttribute("point", 102920122);
        return "func";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("张三",18,new Date(),50F));
        studentList.add(new Student("小红",18,new Date(),500F));
        studentList.add(new Student("小蓝",18,new Date(),5000F));
        studentList.add(new Student("小绿",18,new Date(),50000F));
        model.addAttribute("stus",studentList);


        Map<String,Object> map = new HashMap<>();
        map.put("stu1",new Student("小明",18,new Date(),50000F));
        map.put("stu2",new Student("小红",18,new Date(),500F));
        model.addAttribute("stuMap",map);

        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis()+1000);

        model.addAttribute("date1",date1);
        model.addAttribute("date2",date2);

        model.addAttribute("name","张三");
        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name","张三");
        Student student = new Student();
        student.setName("李四");
        student.setAge(18);
        model.addAttribute("stu",student);
        return "basic";
    }

}
