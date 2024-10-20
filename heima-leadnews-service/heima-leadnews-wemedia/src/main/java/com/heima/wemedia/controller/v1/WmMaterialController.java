package com.heima.wemedia.controller.v1;


import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/v1/material")
@Slf4j
//@Api(tags = "自媒体素材相关接口")
public class WmMaterialController {

    @Resource
    private WmMaterialService wmMaterialService;


    @PostMapping("/list")
    public ResponseResult page(WmMaterialDto dto){
        log.info("分页查询数据:{}",dto);
        return wmMaterialService.pageList(dto);
    }

    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        log.info("素材上传:{}",multipartFile);
        return wmMaterialService.uploadPicture(multipartFile);
    }


}
