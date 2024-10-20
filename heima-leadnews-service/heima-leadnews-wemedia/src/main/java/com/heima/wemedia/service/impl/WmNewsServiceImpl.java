package com.heima.wemedia.service.impl;

import com.aliyuncs.utils.StringUtils;
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
import javassist.compiler.ast.Keyword;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Override
    public ResponseResult pageQuery(WmNewsPageReqDto dto) {
        Long userId = UserContext.getId();
        if (Objects.isNull(userId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        dto.checkParam();

        Short status = dto.getStatus();
        String keyword = dto.getKeyword();
        Integer channelId = dto.getChannelId();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        lambdaQuery().eq(WmNews::getUserId,userId)
                .eq(!Objects.isNull(status),WmNews::getStatus,status)
                .like(StringUtils.isNotEmpty(keyword),WmNews::getTitle,keyword)
                .eq(!Objects.isNull(channelId),WmNews::getChannelId,channelId)
                .between(!Objects.isNull(begin)&&!Objects.isNull(end),WmNews::getPublishTime,begin,end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
