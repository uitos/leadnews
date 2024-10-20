package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author enchanter
 */
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 素材上传
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 分页条件查询
     * @param dto
     * @return
     */
    ResponseResult pageQuery(WmMaterialDto dto);
}
