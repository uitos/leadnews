package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;

/**
 * 频道业务层
 *
 * @author northward
 */
public interface WmChannelService extends IService<WmChannel> {
    /**
     * 查询所有频道
     *
     * @return 频道列表
     */
    ResponseResult listQuery();
}
