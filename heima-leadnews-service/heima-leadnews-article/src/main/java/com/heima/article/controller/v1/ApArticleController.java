package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-05 10:16:52
 */
@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ApArticleController {

    @Autowired
    private ApArticleService apArticleService;

}
