package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api(tags ="自媒体文章")
public class WnNewsController {


    @Resource
    private WmNewsService wmNewsService;

    @ApiOperation(value = "发布文章")
    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.warn("文章提交：{}",dto);
        return wmNewsService.submit(dto);
    }

    @ApiOperation("根据ID查询")
    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable("id") Long id){
        return ResponseResult.okResult(wmNewsService.getById(id));
    }


    @RequestMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.pageList(dto);
    }
}
