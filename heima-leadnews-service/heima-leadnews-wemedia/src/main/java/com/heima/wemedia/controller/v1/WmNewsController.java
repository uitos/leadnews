package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api("素材上传相关接口")
public class WmNewsController {

    @Resource
    WmNewsService wmNewsService;
    @PostMapping("list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.pageQuery(dto);

    }

    @PostMapping("submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        return wmNewsService.submit(dto);

    }

    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable Integer id){
        return ResponseResult.okResult(wmNewsService.getById(id));
    }


}
