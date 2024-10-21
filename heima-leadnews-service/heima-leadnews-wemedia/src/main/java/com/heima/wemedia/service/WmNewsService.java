package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {
    /**
     * 查询频道列表
     *
     * @param dto 频道id
     * @return 频道列表
     */
    ResponseResult pageQuery(WmNewsPageReqDto dto);

    /**
     * 新增修改文章
     *
     * @param dto 文章信息
     * @return 结果
     */
    ResponseResult submit(WmNewsDto dto);
}
