package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api(tags ="自媒体文章")
public class WnNewsController {


    @Resource
    private WmNewsService wmNewsService;


    @RequestMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.pageList(dto);
    }
}
