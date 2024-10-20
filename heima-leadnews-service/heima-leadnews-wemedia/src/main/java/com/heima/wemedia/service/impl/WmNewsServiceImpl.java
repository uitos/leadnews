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
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author mianbao
 * 1
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Override
    public ResponseResult list(WmNewsPageReqDto dto) {

        Long userId = UserContext.getUserId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }

        Page<WmNews> page = lambdaQuery().eq(Objects.nonNull(dto.getStatus()), WmNews::getStatus, dto.getStatus())
                .eq(Objects.nonNull(dto.getChannelId()), WmNews::getChannelId, dto.getChannelId())
                .eq(Objects.nonNull(dto.getStatus()), WmNews::getStatus, dto.getStatus())
                .like(Objects.nonNull(dto.getKeyword()), WmNews::getTitle, dto.getKeyword())
                .between(Objects.nonNull(dto.getBeginPubDate()), WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate())
                .eq(WmNews::getUserId, userId)
                .orderByDesc(WmNews::getCreatedTime)
                .page(new Page<>(dto.getPage(), dto.getSize()));
        PageResponseResult pageResponseResult = new PageResponseResult();
        pageResponseResult.setCurrentPage((int) page.getCurrent());
        pageResponseResult.setTotal((int) page.getTotal());
        pageResponseResult.setSize((int) page.getSize());
        pageResponseResult.setData(page.getRecords());

        return pageResponseResult;
    }
}
