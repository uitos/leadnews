package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    @Autowired
    private WmChannelMapper wmChannelMapper;

    /**
     * 查询所有频道
     * @return
     */
    @Override
    public ResponseResult channels() {
        List<WmChannel> wmChannels = wmChannelMapper.selectList(new LambdaQueryWrapper<WmChannel>().eq(WmChannel::getStatus, 1)
                .orderByAsc(WmChannel::getOrd)
                .orderByDesc(WmChannel::getCreatedTime));
        return ResponseResult.okResult(wmChannels);
    }
}