package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 素材上传
     *
     * @param multipartFile 文件
     * @return 上传结果
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 分页条件查询
     *
     * @param dto 封装查询参数
     * @return 查询结果
     */
    ResponseResult queryPage(WmMaterialDto dto);
}
