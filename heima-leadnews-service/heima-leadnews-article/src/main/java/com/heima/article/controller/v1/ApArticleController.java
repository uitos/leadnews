package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("load")
    public ResponseResult load(ArticleHomeDto dto) {

        return apArticleService.getByArticleHomeDto(dto, null);
    }

    @PostMapping("loadnew")
    public ResponseResult loadnew(ArticleHomeDto dto) {

        return apArticleService.getByArticleHomeDto(dto, (short) 1);
    }

    @PostMapping("loadmore")
    public ResponseResult loadmore(ArticleHomeDto dto) {

        return apArticleService.getByArticleHomeDto(dto, (short) 2);
    }
}
