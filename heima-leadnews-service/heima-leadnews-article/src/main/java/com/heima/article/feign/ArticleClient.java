package com.heima.article.feign;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ArticleClient {
    @Resource
    ApArticleService apArticleService;
    @PostMapping("/save")
    public ResponseResult<Long> save(@RequestBody ArticleDto dto){
        return apArticleService.saveArticle(dto);
    }
}
