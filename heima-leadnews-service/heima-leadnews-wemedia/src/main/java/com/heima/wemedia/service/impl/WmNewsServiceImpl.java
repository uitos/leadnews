package com.heima.wemedia.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.utils.UserContext;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.K;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


/**
 * @ClassName WmNewsServiceImpl
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/20
 * @Version 1.0
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Override
    public ResponseResult pageQuery(WmNewsPageReqDto dto) {
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.checkParam();
        Short status = dto.getStatus();
        String keyword = dto.getKeyword();
        Integer channelId = dto.getChannelId();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        lambdaQuery().eq(WmNews::getUserId, wmUserId)
                .eq(Objects.nonNull(status), WmNews::getStatus, status)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .like(StringUtils.isNotBlank(keyword), WmNews::getTitle, keyword)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        PageResponseResult result = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
