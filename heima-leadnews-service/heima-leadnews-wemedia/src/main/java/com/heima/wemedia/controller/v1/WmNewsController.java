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
@Api(tags = "自媒体文章相关接口")
public class WmNewsController {

    @Resource
    private WmNewsService wmNewsService;

    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.info("上传文章:{}",dto);
        return wmNewsService.submit(dto);
    }


    @ApiOperation("根据ID查询(回显)文章")
    @GetMapping("/one/{id}")
    public ResponseResult findOne(@PathVariable("id") Integer id){
        log.info("文章ID:{}",id);
        return ResponseResult.okResult(wmNewsService.getById(id));
    }

    @ApiOperation("分页条件查询")
    @PostMapping("list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        log.info("dto:{}",dto);
        return wmNewsService.pageQuery(dto);
    }
}
