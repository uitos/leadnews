package com.heima.article.controller.v1.fegin;

import com.heima.article.service.ApArticleService;
import com.heima.model.common.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ArticleClient
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/21
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ArticleClient {

    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/save")
    public ResponseResult<Long> saveArticle(@RequestBody ArticleDto dto) {
        log.info("articleClient saveArticle:{}", dto);
        return apArticleService.saveArticle(dto);
    }
}
