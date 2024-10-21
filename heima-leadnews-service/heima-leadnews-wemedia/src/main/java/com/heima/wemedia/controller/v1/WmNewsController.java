package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 18:08:20
 */
@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api(tags = "自媒体文章相关接口")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    @ApiOperation("发布文章")
    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.warn("dto:{}", dto);
        return wmNewsService.submit(dto);
    }

    @ApiOperation("根据ID查询")
    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable("id") Integer id){
        log.warn("id:{}", id);
        return ResponseResult.okResult(wmNewsService.getById(id));
    }

    @ApiOperation("分页条件查询")
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        log.warn("dto:{}", dto);
        return wmNewsService.pageQuery(dto);
    }

}
