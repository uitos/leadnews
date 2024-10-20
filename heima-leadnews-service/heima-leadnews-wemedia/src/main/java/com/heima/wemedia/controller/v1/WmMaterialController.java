package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Resource
    private WmMaterialService wmMaterialService;

    @PostMapping("upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) throws Exception {
        return wmMaterialService.uploadPicture(multipartFile);
    }

    @PostMapping("list")
    public ResponseResult list(@RequestBody WmMaterialDto dto) {
        return wmMaterialService.queryPage(dto);
    }

}
