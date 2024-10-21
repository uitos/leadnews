package com.heima.wemedia.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.common.dtos.ContentDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @ClassName WmNewsServiceImpl
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/20
 * @Version 1.0
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Override
    public ResponseResult pageQuery(WmNewsPageReqDto dto) {
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.checkParam();
        Short status = dto.getStatus();
        String keyword = dto.getKeyword();
        Integer channelId = dto.getChannelId();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        lambdaQuery().eq(WmNews::getUserId, wmUserId)
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

    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (!Objects.isNull(dto.getId())) {
            //更新, 删除表关系
            wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>()
                    .eq(WmNewsMaterial::getNewsId, dto.getId()));
        }
        WmNews wmNews = createWmNews(dto);
        boolean flag = saveOrUpdate(wmNews);
        if (!flag) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (!Objects.isNull(dto.getStatus()) && dto.getStatus() == 0) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //不是草稿, 保存素材和文章的关系
        List<String> contentImagesUrls = getImagesFromContent(wmNews.getContent());
        saveNewsAndMaterialRelations(wmNews.getId(), contentImagesUrls, WemediaConstants.WM_COVER_REFERENCE);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveNewsAndMaterialRelations(Integer id, List<String> imagesUrls, Short type) {
        if (CollectionUtils.isEmpty(imagesUrls)) {
            return;
        }
        //根据图片URL查询素材ID列表
        List<Integer> materialIds = wmMaterialMapper
                .selectList(new LambdaQueryWrapper<WmMaterial>().in(WmMaterial::getUrl, imagesUrls))
                .stream()
                .map(WmMaterial::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialIds)) {
            return;
        }
        int row = 0;
        try {
            row = wmNewsMaterialMapper.saveNewsAndMaterialRelations(id, materialIds, type);
        } catch (Exception e) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
        if (row == 0) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    private List<String> getImagesFromContent(String content) {
        if (StringUtils.isNotBlank(content)) {
            return null;
        } else {
            List<ContentDto> list = JSON.parseArray(content, ContentDto.class);
            return list.stream()
                    .filter(item -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(item.getType()))
                    .map(ContentDto::getType)
                    .collect(Collectors.toList());
        }
    }

    private WmNews createWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> imagesUrls = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            imagesUrls = getImagesFromContent(dto.getContent());
            if (CollectionUtils.isEmpty(imagesUrls)) {
                //无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else if (imagesUrls.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                //三图
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                imagesUrls = imagesUrls.stream()
                        .limit(WemediaConstants.WM_NEWS_MANY_IMAGE)
                        .collect(Collectors.toList());
            } else {
                //单图
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                imagesUrls = imagesUrls.stream()
                        .limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE)
                        .collect(Collectors.toList());
            }
            dto.setImages(imagesUrls);
        }
        wmNews.setImages(StringUtils.join(imagesUrls, ","));
        wmNews.setEnable((short) 1);
        return wmNews;
    }
}
