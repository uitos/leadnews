package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ChannelServiceImpl
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/20
 * @Version 1.0
 */
@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    @Override
    public ResponseResult listQuery() {
        List<WmChannel> list = lambdaQuery()
                .eq(WmChannel::getStatus, 1)
                .orderByAsc(WmChannel::getOrd)
                .orderByDesc(WmChannel::getCreatedTime)
                .list();
        return ResponseResult.okResult(list);
    }
}
