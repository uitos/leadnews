package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wm.dtos.WmNewsDto;
import com.heima.model.wm.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
      log.info("分页查询文章列表获取到的dto:{}",dto);
        return wmNewsService.findAllByStatus(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.info("文章提交获取到的dto:{}",dto);
        return wmNewsService.submit(dto);
    }

    @GetMapping("/one/{id}")
    public ResponseResult findNewsById(@PathVariable("id") Integer newsId){
        return wmNewsService.selectByNewsId(newsId);
    }

}
