package com.heima.wemedia.feign;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-07 15:09:09
 */
@RestController
@Slf4j
public class WemediaClient {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/inner/api/v1/channel/list")
    public ResponseResult<List<WmChannel>> channels(){
        log.warn("WmChannelController list");
        return ResponseResult.okResult(wmChannelService.list());
    }

}
