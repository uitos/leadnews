package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "文章管理")
public class ApArticleController {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 加载首页
     * @param dto
     * @return
     */
    @PostMapping("/load")
    @ApiOperation("加载首页")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        log.info("dto{}",dto);
        return apArticleService.listByArticleHomeDto(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

    /**
     * 加载首页
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    @ApiOperation("加载首页")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        log.info("dto{}",dto);
        return apArticleService.listByArticleHomeDto(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

    /**
     * 加载首页
     * @param dto
     * @return
     */
    @PostMapping("/loadmore")
    @ApiOperation("加载首页")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        log.info("dto{}",dto);
        return apArticleService.listByArticleHomeDto(dto,ArticleConstants.LOADTYPE_LOAD_MORE);
    }

}
