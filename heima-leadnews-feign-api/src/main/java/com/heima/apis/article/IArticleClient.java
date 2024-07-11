package com.heima.apis.article;

import com.heima.apis.article.fallback.ArticleClientFallback;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-08 16:49:46
 */
@FeignClient(name = "leadnews-article", fallback = ArticleClientFallback.class)
public interface IArticleClient {

    /**
     * 保存或更新文章
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/article/save")
    ResponseResult saveApArticle(@RequestBody ArticleDto dto);

}
