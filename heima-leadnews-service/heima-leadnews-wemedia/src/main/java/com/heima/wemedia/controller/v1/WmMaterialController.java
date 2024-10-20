package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
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
public class WmMaterialController {
    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 分页条件查询素材
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialDto dto) {
        log.warn("dto:{}" , dto);
        return wmMaterialService.pageQuery(dto);
    }

    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        log.warn("素材上传", multipartFile);
        return wmMaterialService.uploadPicture(multipartFile);
    }



}
