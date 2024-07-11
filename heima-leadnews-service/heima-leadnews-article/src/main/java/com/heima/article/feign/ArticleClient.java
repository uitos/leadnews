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

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-08 16:52:16
 */
@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ArticleClient {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 保存或更新文章
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public ResponseResult saveApArticle(@RequestBody ArticleDto dto) {
        log.warn("dto:{}", dto);
        return apArticleService.saveApArticle(dto);
    }

}
