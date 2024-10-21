package com.heima.apis.article;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("leadnews-article")
public interface IArticleClient {

    @PostMapping("/api/v1/article/save")
    ResponseResult<Long> save(@RequestBody ArticleDto dto);

}
