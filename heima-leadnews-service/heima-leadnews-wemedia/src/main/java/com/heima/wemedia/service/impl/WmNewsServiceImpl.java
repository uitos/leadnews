package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.article.dtos.ContentDto;
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
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Resource
    WmNewsMaterialMapper wmNewsMaterialMapper;
    @Resource
    WmNewsMapper wmNewsMapper;
    @Resource
    WmMaterialMapper wmMaterialMapper;

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
        lambdaQuery()
                .eq(WmNews::getUserId, wmUserId)
                .eq(Objects.nonNull(status), WmNews::getStatus, status)
                .like(StringUtils.isNotBlank(keyword), WmNews::getTitle, keyword)
                .eq(Objects.nonNull(channelId), WmNews::getChannelId, channelId)
                .between(Objects.nonNull(begin) && Objects.nonNull(end), WmNews::getPublishTime, begin, end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);

        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        //参数校验
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (Objects.isNull(UserContext.getId())) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //判断是更新还是删除，更新前需要删除表中所有关系
        if (Objects.nonNull(dto.getId())) {
            wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>().eq(WmNewsMaterial::getNewsId, dto.getId()));
        }

        WmNews wmNews = createWmNews(dto);
        boolean flag = saveOrUpdate(wmNews);
        if(!flag){
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }

        if (Objects.nonNull(dto.getStatus()) && dto.getStatus() == 0) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        Integer id=wmNews.getId();
        List<String> contentUrls = getContentImages(dto.getContent());
        saveMaterialAndNewsRelationship(id, contentUrls, WemediaConstants.WM_CONTENT_REFERENCE);

        List<String> coverUrls=dto.getImages();
        saveMaterialAndNewsRelationship(id,coverUrls,WemediaConstants.WM_COVER_REFERENCE);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveMaterialAndNewsRelationship(Integer id, List<String> imageUrls, Short type) {
        if (imageUrls.isEmpty()) {
            return;
        }

        List<Integer> materialIds = wmMaterialMapper.selectList(
                new LambdaQueryWrapper<WmMaterial>()
                .in(WmMaterial::getUrl,imageUrls)
        ).stream().map(WmMaterial::getId).collect(Collectors.toList());
        int row = wmNewsMaterialMapper.batchInsert(id, materialIds, type);
        if(row==0){
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }


    }

    private WmNews createWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        wmNews.setId(dto.getId());
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setTitle(dto.getTitle());
        wmNews.setContent(dto.getContent());
        wmNews.setChannelId(dto.getChannelId());
        wmNews.setLabels(dto.getLabels());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(dto.getSubmitedTime());
        wmNews.setStatus(dto.getStatus());
        wmNews.setPublishTime(dto.getPublishTime());
        wmNews.setEnable((short) 1);

        wmNews.setType(dto.getType());
        List<String> imageUrls = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            imageUrls = getContentImages(dto.getContent());
            if (imageUrls.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                imageUrls = imageUrls.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE).collect(Collectors.toList());
            } else if (imageUrls.size() == WemediaConstants.WM_NEWS_NONE_IMAGE) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
            } else {
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                imageUrls = imageUrls.stream().limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE).collect(Collectors.toList());
            }
            dto.setImages(imageUrls);
        }
        wmNews.setImages(StringUtils.join(imageUrls, ","));
        return wmNews;


    }

    private List<String> getContentImages(String content) {
        //根据content中内容的图片构建一个集合 没有则为空集
        if (StringUtils.isBlank(content)) {
            return Collections.emptyList();
        }
        List<ContentDto> contentDtos = JSON.parseArray(content, ContentDto.class);
        return contentDtos.stream().filter(c -> "image".equals(c.getType())).map(ContentDto::getValue).collect(Collectors.toList());
    }
}
