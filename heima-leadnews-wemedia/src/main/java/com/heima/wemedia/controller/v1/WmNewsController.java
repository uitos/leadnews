package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author enchanter
 */
@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@Api(tags = "自媒体文章相关接口")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    @ApiOperation("分页条件查询")
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmNewsPageReqDto dto){
        log.warn("dto:{}", dto);
        return wmNewsService.pageQuery(dto);
    }

    @PostMapping("/submit")
    public ResponseResult insertOrUpdate( @RequestBody WmNewsDto dto ){
        return   wmNewsService.submit(dto);


    }



    /**
     * 根据id查询文章详情
     * @param id 文章id
     * @return 文章详情
     */
    @PostMapping("one/{id}")
    public ResponseResult one(@PathVariable("id") Integer id){
        return ResponseResult.okResult(wmNewsService.getById(id));
    }

}
