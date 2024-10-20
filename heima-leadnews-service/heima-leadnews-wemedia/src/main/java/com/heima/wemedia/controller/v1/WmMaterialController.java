package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/material")
@Slf4j
@Api("素材上传相关接口")
public class WmMaterialController {

    @Resource
    WmMaterialService wmMaterialService;
    @PostMapping("/upload_picture")
    @ApiOperation("图片上传接口")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);

    }

    @ApiOperation("分页查询")
    @PostMapping("list")
    public ResponseResult list(@RequestBody WmMaterialDto dto){
        return wmMaterialService.queryByPage(dto);
    }



}
