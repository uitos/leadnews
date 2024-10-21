package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.impl.WmNewsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author mianbao
 * 1
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Resource
    private WmNewsService wmNewsService;
    @Autowired
    private WmNewsServiceImpl wmNewsServiceImpl;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto) {
        log.info("dto:{}", dto);
        return wmNewsService.list(dto);

    }

    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable Integer id) {
        return ResponseResult.okResult(wmNewsServiceImpl.getById(id));

    }
}
