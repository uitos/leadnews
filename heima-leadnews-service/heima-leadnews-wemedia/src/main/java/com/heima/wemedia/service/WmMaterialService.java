package com.heima.wemedia.service;


import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author mianbao
 */
public interface WmMaterialService {
    ResponseResult upload(MultipartFile file) throws IOException;

    ResponseResult selectByPage(WmMaterialDto dto);

}
