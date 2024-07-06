package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 14:58:21
 */
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 上传素材
     * @param multipartFile
     * @return 数据：素材详细信息
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 分页条件查询
     * @param dto
     * @return
     */
    ResponseResult findPage(WmMaterialDto dto);
}
