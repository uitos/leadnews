package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
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
 * @date 2024-07-05 10:16:52
 */
@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ApArticleController {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 加载文章首页
     */
    @PostMapping("load")
    public ResponseResult loadArticle(@RequestBody ArticleHomeDto dto) {
        return apArticleService.loadArticle(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    @PostMapping("loadnew")
    public ResponseResult loadNewArticle(@RequestBody ArticleHomeDto dto) {
        return apArticleService.loadArticle(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

    @PostMapping("loadmore")
    public ResponseResult loadMoreArticle(@RequestBody ArticleHomeDto dto) {
        return apArticleService.loadArticle(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

}
