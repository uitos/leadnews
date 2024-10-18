package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
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
     * 加载首页数据
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        log.info("加载首页数据{}",dto);
        return apArticleService.load(dto);
    }

    /**
     * 加载首页最新数据
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        log.info("加载首页最新数据{}",dto);
        return apArticleService.load(dto);
    }

    /**
     * 加载首页更多数据
     * @param dto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        log.info("加载首页更多数据{}",dto);
        return apArticleService.load(dto);
    }



}
