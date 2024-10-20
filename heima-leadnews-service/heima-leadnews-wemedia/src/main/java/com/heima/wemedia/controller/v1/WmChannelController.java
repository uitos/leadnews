package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channel")
    public ResponseResult channels() {
        log.warn("查询所有频道");
        return wmChannelService.listQuery();
    }
}
