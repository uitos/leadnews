package com.heima.user.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-05 09:13:38
 */
@RestController
@Slf4j
public class AjaxController {

    @GetMapping("/ajax")
    public String testAjax(){
        System.out.println("testAjax---》");
        return "Gateway Handle Cross";
    }

}
