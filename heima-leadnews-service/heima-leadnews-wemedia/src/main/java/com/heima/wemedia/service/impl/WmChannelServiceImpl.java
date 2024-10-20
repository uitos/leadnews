package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    @Override
    public ResponseResult listQuery() {
        List<WmChannel> list = lambdaQuery().eq(WmChannel::getStatus, 1)
                .orderByAsc(WmChannel::getOrd)
                .orderByDesc(WmChannel::getCreatedTime)
                .list();
        return ResponseResult.okResult(list);

    }
}
