package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/material")
@Slf4j
@Api(tags = "自媒体素材相关接口")
public class WmMaterialController {

    @Resource
    private WmMaterialService wmMaterialService;

    @ApiOperation("素材列表分页查询")
    @PostMapping("/list")
    public ResponseResult list(WmMaterialDto dto){
        return wmMaterialService.pageQuery(dto);

    }


    @ApiOperation("素材上传")
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        log.info("素材上传：{}",multipartFile);
        return wmMaterialService.uploadPicture(multipartFile);
    }

}
