package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
@Api(tags ="频道相关接口")
public class WmChannelController {

    @Resource
    private WmChannelService wmChannelService;

    @ApiOperation("查询所以频道")
    @GetMapping("/channels")
    public ResponseResult channels() {


        return wmChannelService.listQuery();

    }
}
