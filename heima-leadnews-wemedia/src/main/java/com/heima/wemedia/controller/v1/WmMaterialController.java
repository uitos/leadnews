package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author enchanter
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Resource
    private WmMaterialService wmMaterialService;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialDto dto) {
        log.warn("dto:{}", dto);
        return wmMaterialService.pageQuery(dto);
    }

    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        log.info("上传图片{}", multipartFile);
        return wmMaterialService.uploadPicture(multipartFile);
    }

}
