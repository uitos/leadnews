package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.ChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/v1/channel")
public class ChannelController {

    @Resource
    private ChannelService channelService;

    @GetMapping("channels")
    public ResponseResult channels(){
        return channelService.channels();
    }
}
