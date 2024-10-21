package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.wemedia.service.WmNewService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {
    @Resource
    private WmNewService wmNewService;

    @PostMapping("submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        return wmNewService.submit(dto);
    }

    @GetMapping("/one/{id}")
    public ResponseResult findOne(@PathVariable("id") Integer id){
        return ResponseResult.okResult(wmNewService.getById(id));
    }
}
