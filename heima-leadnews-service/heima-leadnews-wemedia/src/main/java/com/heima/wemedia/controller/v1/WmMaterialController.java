package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 14:56:10
 */
@RestController
@RequestMapping("/api/v1/material")
@Slf4j
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/del_picture/{id}")
    public ResponseResult delPicture(@PathVariable("id") Integer id){
        return wmMaterialService.delPicture(id);
    }

    /**
     * 收藏
     * @param id
     * @return
     */
    @GetMapping("/collect/{id}")
    public ResponseResult collect(@PathVariable("id") Integer id){
        return wmMaterialService.isCollect(id, (short) 1);
    }

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollect(@PathVariable("id") Integer id){
        return wmMaterialService.isCollect(id, (short) 0);
    }

    /**
     * 分页显示素材
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialDto dto){
        log.info("dto:{}", dto);
        return wmMaterialService.findPage(dto);
    }

    /**
     * 上传素材
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        log.info("multipartFile:{}", multipartFile);
        return wmMaterialService.uploadPicture(multipartFile);
    }

}