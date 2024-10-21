package com.heima.article.controller.feign;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章客户端
 *
 * @Name ArticleClient
 * @Author viktor
 * @Date 2024-10-21 16:55
 */
@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ArticleClient {

    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/save")
    public ResponseResult<Long> saveArticle(@RequestBody ArticleDto dto) {
        log.info("dto:{}",dto);
        return apArticleService.saveArticle(dto);
    }
}