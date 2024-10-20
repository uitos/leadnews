package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.channels.Channel;

@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
@Api("素材上传相关接口")
public class WmChannelController {

    @Resource
    WmChannelService wmChannelService;
    @GetMapping("/channels")
    public ResponseResult channels(){
        return wmChannelService.listQuery();

    }


}
