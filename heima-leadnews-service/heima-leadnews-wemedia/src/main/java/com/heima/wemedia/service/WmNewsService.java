package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wm.dtos.WmNewsDto;
import com.heima.model.wm.dtos.WmNewsPageReqDto;
import com.heima.model.wm.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {


    ResponseResult findAllByStatus(WmNewsPageReqDto dto);

    ResponseResult submit(WmNewsDto dto);

    ResponseResult selectByNewsId(Integer newsId);

}