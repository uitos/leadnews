package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WmNewsController
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/20
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;


    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto) {
        log.info("提交文章:{}", dto);
        return wmNewsService.submit(dto);
    }

    @GetMapping("/one/{id}")
    public ResponseResult findOne(@PathVariable("id") Integer id) {
        log.info("id:{}", id);
        return ResponseResult.okResult(wmNewsService.getById(id));
    }

    @PostMapping("/list")
    public ResponseResult list(WmNewsPageReqDto dto) {
        log.info("查询文章列表:{}", dto);
        return wmNewsService.pageQuery(dto);
    }


}
