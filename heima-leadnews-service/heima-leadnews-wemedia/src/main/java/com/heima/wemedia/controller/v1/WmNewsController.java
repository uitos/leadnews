package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api(tags = "自媒体文章相关接口")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    @PostMapping("/submit")
    @ApiOperation("发布文章")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.warn("dto:{}",dto);
        return wmNewsService.submit(dto);
    }

    @GetMapping("/one/{id}")
    @ApiOperation("根据id查询文章")
    public ResponseResult one(@PathVariable("id")Integer id){
        log.info("id:{}",id);
        return ResponseResult.okResult(wmNewsService.getById(id));
    }
    @PostMapping("/list")
    @ApiOperation("分页条件查询")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        log.info("dto:{}",dto);
        return wmNewsService.pageQuery(dto);
    }
}
