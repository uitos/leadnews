package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author enchanter
 */
@RestController("/api/v1")
public class WmMaterialController {

    @Resource
    private WmMaterialService wmMaterialService;

    @PostMapping("/material/upload_picture")
    public ResponseResult uploadPicture(@RequestBody MultipartFile file) {
        return wmMaterialService.uploadPicture(file);
    }

}
