package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wm.dtos.WmNewsDto;
import com.heima.model.wm.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api(tags = "自媒体文章相关接口")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;
    @PostMapping("/list")
    @ApiOperation("分页查询文章列表")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
      log.info("分页查询文章列表获取到的dto:{}",dto);
        return wmNewsService.findAllByStatus(dto);
    }

    @ApiOperation("提交文章")
    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        log.info("文章提交获取到的dto:{}",dto);
        return wmNewsService.submit(dto);
    }

    @ApiOperation("通过Id查询文章信息")
    @GetMapping("/one/{id}")
    public ResponseResult findNewsById(@PathVariable("id") Integer newsId){
        return wmNewsService.selectByNewsId(newsId);
    }



    @ApiOperation("上/下架文章")
    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto){
        log.info("文章提交获取到的dto:{}",dto);
        return wmNewsService.downOrUp(dto);
    }

    @ApiOperation("删除文章信息")
    @GetMapping("/del_news/{id}")
    public ResponseResult deleteNewsById(@PathVariable("id") Integer newsId){
        return wmNewsService.deleteByNewsId(newsId);
    }


}
