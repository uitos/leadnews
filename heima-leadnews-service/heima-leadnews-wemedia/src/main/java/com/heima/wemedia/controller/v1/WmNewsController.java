package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {
    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 文章分页条件查询
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult pageQuery(@RequestBody WmNewsPageReqDto dto){
        log.info("查询文章列表{}",dto);
        return wmNewsService.pageQuery(dto);
    }

    /**
     * 根据文章id查询文章详情
     * @return
     */
    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable Long id){
        log.info("根据文章id查询文章详情{}",id);
        WmNews wmNews = wmNewsService.lambdaQuery().eq(WmNews::getId, id).one();
        return ResponseResult.okResult(wmNews);
    }
    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.info("提交文章{}",dto);
        return wmNewsService.submit(dto);
//        return ResponseResult.okResult(dto);
    }
}
