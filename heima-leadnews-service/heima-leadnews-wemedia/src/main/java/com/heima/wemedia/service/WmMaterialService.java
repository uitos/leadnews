package com.heima.wemedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wm.dtos.WmMaterialDto;
import com.heima.model.wm.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {


    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    ResponseResult findByPage(WmMaterialDto dto);

    ResponseResult collect(Integer materialId);

    ResponseResult cancelCollect(Integer materialId);

    ResponseResult deleteByMaterialId(Integer materialId);

}