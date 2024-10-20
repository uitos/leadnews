package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WmNewMapper;
import com.heima.wemedia.service.WmNewService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class WmNewImpl extends ServiceImpl<WmNewMapper, WmNews> implements WmNewService {

    @Override
    public ResponseResult queryPage(WmNewsPageReqDto dto) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long userId = UserContext.getId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.checkParam();
        Short status = dto.getStatus();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Integer channelId = dto.getChannelId();
        String keyword = dto.getKeyword();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                .eq(WmNews::getUserId, userId)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .like(!Objects.isNull(keyword), WmNews::getTitle, keyword)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        // 设置查询结果到分页响应结果对象中
        pageResponseResult.setData(page.getRecords());
        // 返回分页响应结果
        return pageResponseResult;
    }
}
