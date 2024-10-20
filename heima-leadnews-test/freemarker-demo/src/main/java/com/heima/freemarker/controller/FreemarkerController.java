package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        List<Student> students = new ArrayList<>();
        students.add(new Student("张三",12,null,3000F));
        students.add(new Student("李四",12,null,3000F));
        students.add(new Student("王五",33,null,12000F));
        model.addAttribute("stus",students);

        model.addAttribute("today",new Date());

        model.addAttribute("point",1231241.124);


        return "func";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Student> students=new ArrayList<>();
        students.add(new Student("张三",12,null,3000F));
        students.add(new Student("李四",18,null,4000F));
        model.addAttribute("stus",students);

        Map<String,Student> studentMap=new HashMap();
        studentMap.put("stu1",new Student("王五",33,null,12000F));
        studentMap.put("stu2",new Student("钱六",23,null,12000F));
        model.addAttribute("stuMap",studentMap);

        model.addAttribute("date1","2021-01-10");
        model.addAttribute("date2","2021-01-11");

        model.addAttribute("name","张三");

        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){
        model.addAttribute("name","张三");
        Student student=new Student();
        student.setName("张三");
        student.setAge(18);
        model.addAttribute("stu",student);

        return "basic";
    }

}
