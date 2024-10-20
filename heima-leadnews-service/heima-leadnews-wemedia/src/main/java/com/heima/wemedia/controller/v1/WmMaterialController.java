package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wm.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
@Slf4j
@Api(tags = "自媒体素材相关接口")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;
    @PostMapping("/upload_picture")
    @ApiOperation("上传素材图片文件")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        log.info("上传图片文件:{}",multipartFile);
        return wmMaterialService.uploadPicture(multipartFile);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询素材图片文件")
    public ResponseResult list(@RequestBody WmMaterialDto dto){
        log.info("分页查询素材列表:{}",dto);
        return wmMaterialService.findByPage(dto);
    }

    @GetMapping("/collect/{id}")
    @ApiOperation("收藏素材图片文件")
    public ResponseResult collect(@PathVariable("id") Integer materialId){
        log.info("收藏素材:{}",materialId);
        return wmMaterialService.collect(materialId);
    }


    @ApiOperation("取消收藏素材图片文件")
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollect(@PathVariable("id") Integer materialId){
        return wmMaterialService.cancelCollect(materialId);
    }


    @GetMapping("/del_picture/{id}")
    @ApiOperation("删除素材图片文件")
    public ResponseResult findNewsById(@PathVariable("id") Integer materialId){
        return wmMaterialService.deleteByMaterialId(materialId);
    }
}