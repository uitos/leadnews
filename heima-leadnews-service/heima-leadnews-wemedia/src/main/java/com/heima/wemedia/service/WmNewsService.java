package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 18:07:21
 */
public interface WmNewsService extends IService<WmNews> {

    /**
     * 分页条件查询
     * @param dto
     * @return
     */
    ResponseResult pageQuery(WmNewsPageReqDto dto);

    /**
     * 发布文章
     * @param dto 文章数据
     * @return
     */
    ResponseResult submit(WmNewsDto dto);
}
