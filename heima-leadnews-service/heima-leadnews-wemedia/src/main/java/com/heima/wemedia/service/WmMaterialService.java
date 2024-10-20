package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 素材上传
     * @param multipartFile
     * @return
     */
    ResponseResult updatePicture(MultipartFile multipartFile);

    /**
     * 素材分页
     * @param dto
     * @return
     */
    ResponseResult picturePage(WmMaterialDto dto);
}