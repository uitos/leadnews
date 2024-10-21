package com.apis.article;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@FeignClient(name = "leadnews-article")
public interface IArticleClient {

    @PostMapping("/api/v1/article/save")
    ResponseResult<Long> saveArticle(@RequestBody WmNewsDto dto);
}
