package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 17:59:36
 */
public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查询可用的频道列表
     * @return
     */
    ResponseResult listQuery();
}
