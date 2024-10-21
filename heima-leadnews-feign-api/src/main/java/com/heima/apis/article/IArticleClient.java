package com.heima.apis.article;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * article远程调用客户端
 *
 * @Name IArticleClient
 * @Author viktor
 * @Date 2024-10-21 16:48
 */
@FeignClient(name = "leadnews-article")
public interface IArticleClient {

    @PostMapping("/save")
    ResponseResult<Long> saveArticle(@RequestBody ArticleDto dto);

}