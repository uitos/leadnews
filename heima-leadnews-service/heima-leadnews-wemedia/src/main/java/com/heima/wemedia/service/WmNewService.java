package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewService extends IService<WmNews> {
    ResponseResult queryPage(WmNewsPageReqDto dto);

    /**
     * 发布文章
     * @param dto
     * @return
     */
    ResponseResult submit(WmNewsDto dto);
}
