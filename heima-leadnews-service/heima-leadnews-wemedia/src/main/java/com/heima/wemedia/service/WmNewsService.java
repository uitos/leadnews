package com.heima.wemedia.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;

public interface WmNewsService {
    ResponseResult list(WmNewsPageReqDto dto);
}
