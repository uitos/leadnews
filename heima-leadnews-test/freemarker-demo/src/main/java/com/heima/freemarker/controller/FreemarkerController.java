package com.heima.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        return "list";
    }

    @GetMapping("/basic")
    public String basic(Model model){

        return "basic";
    }

}
