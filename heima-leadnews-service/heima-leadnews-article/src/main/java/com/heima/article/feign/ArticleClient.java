package com.heima.article.feign;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/v1/article")
public class ArticleClient {
    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto){
        log.info("articleClient saveArticle dto:{}",dto);
        return apArticleService.saveArticle(dto);
    }
}
