package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsEnableDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 17:47:14
 */
public interface WmNewsService extends IService<WmNews> {
    /**
     * 分页条件查询
     * @param dto
     * @return
     */
    ResponseResult findPage(WmNewsPageReqDto dto);

    /**
     * 文章发布
     * @param dto
     * @return
     */
    ResponseResult submit(WmNewsDto dto);

    /**
     * 上下架
     * @param dto
     * @return
     */
    ResponseResult downOrUp(WmNewsEnableDto dto);
}
