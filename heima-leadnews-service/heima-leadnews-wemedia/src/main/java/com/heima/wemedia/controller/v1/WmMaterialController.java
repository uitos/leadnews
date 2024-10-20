package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
@Slf4j
@Api(tags = "自媒体素材管理")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wemediaService;

    /**
     * 上传素材
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload_picture")
    @ApiOperation("上传素材")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        log.info("multipartFile:{}", multipartFile);
        return wemediaService.uploadPicture(multipartFile);
    }

    @PostMapping("/list")
    @ApiOperation("素材列表")
    public ResponseResult list(@RequestBody WmMaterialDto dto){
        log.info("dto:{}", dto);
        return wemediaService.findPage(dto);
    }

}