package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/v1/news")
public class WmNewController {

    @Resource
    private WmNewService wmNewService;

    @PostMapping("list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto) {
        return wmNewService.queryPage(dto);
    }

}
