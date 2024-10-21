package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.article.dtos.contentDto;
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
import com.heima.wemedia.service.WmNewsMaterialService;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Resource
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Resource
    private WmMaterialMapper wmMaterialMapper;

    @Override
    public ResponseResult pageList(WmNewsPageReqDto dto) {
        dto.checkParam();
        Long vmUserId = UserContext.getId();
        if (Objects.isNull(vmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        Short status = dto.getStatus();
        String keyword = dto.getKeyword();
        Integer channelId = dto.getChannelId();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        lambdaQuery()
                .eq(WmNews::getUserId, vmUserId)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .like(!Objects.isNull(keyword), WmNews::getTitle, keyword)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);

        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        //校验参数
        Long vmUserId = UserContext.getId();
        if (Objects.isNull(vmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (!Objects.isNull(dto.getId())) {
            wmNewsMaterialMapper.delete(
                    new LambdaQueryWrapper<WmNewsMaterial>()
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

        List<String> contentImageUrls = getImagesFromContent(wmNews.getContent());
        saveNewsAndMaterialRelations(wmNews.getId(), contentImageUrls, WemediaConstants.WM_CONTENT_REFERENCE);

        List<String> coverImageUrls = dto.getImages();
        saveNewsAndMaterialRelations(wmNews.getId(), coverImageUrls, WemediaConstants.WM_COVER_REFERENCE);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);


    }

    private void saveNewsAndMaterialRelations(Integer newsId, List<String> imageUrls, Short type) {
        if (CollectionUtils.isEmpty(imageUrls)){
            return;
        }
        List<Integer> materialIds = wmMaterialMapper.selectList(new LambdaQueryWrapper<WmMaterial>().in(WmMaterial::getUrl, imageUrls))
                .stream()
                .map(WmMaterial::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialIds)){
            return;
        }
        int row = 0;
        try {
            row=wmNewsMaterialMapper.saveNewsAndMaterialRelations(newsId, materialIds, type);
        }catch (Exception e){
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (row==0){
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
    }

    private List<String> getImagesFromContent(String content) {
        if (StringUtils.isNotBlank(content)) {
            return new ArrayList<>();
        } else {
            List<contentDto> list = JSON.parseArray(content, contentDto.class);
            return list.stream().
                    filter(item->WemediaConstants.WM_NEWS_NONE_IMAGE.equals(item.getType()))
                    .map(contentDto::getValue)
                    .collect(Collectors.toList());

        }
    }

    private WmNews createWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> images = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            images = getImagesFromContent(dto.getContent());
            if (CollectionUtils.isEmpty(images)) {
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else if (images.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = images.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE).collect(Collectors.toList());
            }
            dto.setImages(images);
        }
        wmNews.setImages(StringUtils.join(images, ","));
        wmNews.setEnable((short) 1);
        return wmNews;
    }


}
