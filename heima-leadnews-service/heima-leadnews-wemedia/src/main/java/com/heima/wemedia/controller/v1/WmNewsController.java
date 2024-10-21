package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/news")
@Slf4j
public class WmNewsController {
    /**
     * 自媒体文章相关接口
     */
    @Autowired
    private WmNewsService wmNewsService;

    /**
     *根据id查询
     * @param id
     * @return
     */
    @PostMapping("/submit")
   public ResponseResult submit(Boolean draft ,@RequestBody WmNewsDto dto) {
        log.warn("draft:{} , dto:{}", draft , dto);
        return wmNewsService.submit(draft , dto);
    }

    @GetMapping("/one/{id}")
    public ResponseResult one(@PathVariable("id") Integer id) {
        log.warn("查询文章id:{}", id);
        return ResponseResult.okResult(wmNewsService.getById(id));
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto) {
        log.warn("dto:{}", dto);
        return wmNewsService.pageQuery(dto);

    }


}
