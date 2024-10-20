package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/channel")
@RestController
@Slf4j
@Api(tags = "频道相关接口")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @ApiOperation("查询可用频道")
    @GetMapping("/channels")
    public ResponseResult getChannels(){
        log.info("查询所有频道");
        return wmChannelService.listQuery();
    }
}
