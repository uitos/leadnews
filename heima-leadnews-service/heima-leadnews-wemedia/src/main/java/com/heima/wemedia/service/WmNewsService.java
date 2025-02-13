package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {


    /**
     * 文章分页条件查询
     * @param dto
     * @return
     */
    ResponseResult pageQuery(WmNewsPageReqDto dto);

    ResponseResult submit(WmNewsDto dto);
}
