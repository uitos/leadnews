package com.heima.apis.article;

import com.heima.model.common.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "leadnews-article")
public interface IArticleClient {
    @PostMapping("/api/v1/article/save")
    ResponseResult<Long> saveArticle(@RequestBody ArticleDto dto);
}
