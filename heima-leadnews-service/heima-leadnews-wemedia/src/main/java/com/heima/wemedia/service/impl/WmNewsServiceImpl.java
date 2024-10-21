package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.heima.wemedia.service.WmNewsMaterialService;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Diff;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
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
        lambdaQuery()
                .eq(WmNews::getUserId, wmUserId)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .like(StringUtils.isNotBlank(keyword), WmNews::getTitle, keyword)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        PageResponseResult pageResponseResult = new PageResponseResult(
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }


    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        //1.校验参数
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (!Objects.isNull(dto.getId())) {
            wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>()
                    .eq(WmNewsMaterial::getNewsId, dto.getId()));
        }
        //保存或更新文章
        WmNews wmNews = createWmNews(dto);
        boolean flag = saveOrUpdate(wmNews);
        if (!flag) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (!Objects.isNull(dto.getStatus()) && dto.getStatus() == 0) {
            //是草稿直接结束
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //不是草稿，保存文章与素材的关系
        //内容与素材的关系
        List<String> contentImageUrls = getImagesFromContent(wmNews.getContent());
        saveNewsAndMaterRelations(wmNews.getId(), contentImageUrls, WemediaConstants.WM_CONTENT_REFERENCE);
        //图片与素材的关系
        List<String> coverImageUrls = dto.getImages();
        saveNewsAndMaterRelations(wmNews.getId(), coverImageUrls, WemediaConstants.WM_COVER_REFERENCE);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 构造自媒体文章实体
     *
     * @param dto
     * @return
     */
    private WmNews createWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> imageUrls = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            //封面选择自动
            imageUrls = getImagesFromContent(dto.getContent());
            //无图
            if (CollectionUtils.isEmpty(imageUrls)) {
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
                //三图
            } else if (imageUrls.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                imageUrls = imageUrls.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE)
                        .collect(Collectors.toList());
                //单图
            } else {
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                imageUrls = imageUrls.stream().limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE)
                        .collect(Collectors.toList());
            }
            dto.setImages(imageUrls);
        }
        wmNews.setImages(StringUtils.join(imageUrls, ","));
        wmNews.setEnable((short) 1);
        return wmNews;
    }

    private List<String> getImagesFromContent(String content) {
        if (StringUtils.isBlank(content)) {
            return new ArrayList<>();
        } else {
            List<ContentDto> list = JSON.parseArray(content, ContentDto.class);
            return list.stream()
                    //过滤type="image的数据"
                    .filter(item -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(item.getType()))
                    //获取value的值
                    .map(ContentDto::getValue)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 批量保存
     *
     * @param newsId
     * @param imageUrls
     * @param type
     */
    private void saveNewsAndMaterRelations(Integer newsId, List<String> imageUrls, Short type) {
        if (CollectionUtils.isEmpty(imageUrls)) {
            return;
        }
        List<Integer> materialIds = wmMaterialMapper.selectList(new LambdaQueryWrapper<WmMaterial>()
                        .in(WmMaterial::getUrl, imageUrls))
                .stream().map(WmMaterial::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialIds)) {
            return;
        }
        int row = 0;
        try {
            //保存关联关系
            row = wmNewsMaterialMapper.saveNewsAndMaterRelations(newsId, materialIds, type);
        } catch (Exception e) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (row == 0) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
    }
}
