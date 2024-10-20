package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wm.pojos.WmChannel;
import com.heima.model.wm.pojos.WmUser;

public interface WmChannelService extends IService<WmChannel> {


    ResponseResult findAll();

}