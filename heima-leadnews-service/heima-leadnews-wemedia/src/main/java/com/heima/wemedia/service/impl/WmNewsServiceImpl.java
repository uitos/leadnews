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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    private final WmNewsMapper wmNewsMapper;
    private final WmMaterialMapper wmMaterialMapper;
    private final WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 文章分页条件查询
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult pageQuery(WmNewsPageReqDto dto) {
        dto.checkParam();
        Long userId = UserContext.getId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        Short status = dto.getStatus();
        String keyword = dto.getKeyword();
        Integer channelId = dto.getChannelId();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        lambdaQuery()
                .eq(WmNews::getUserId, userId)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .like(!Objects.isNull(keyword), WmNews::getTitle, keyword)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //有id则是更新
        if (!Objects.isNull(dto.getId())) {
            //更新文章 删除关联表数据
            wmNewsMaterialMapper.delete(
                    new LambdaQueryWrapper<WmNewsMaterial>()
                            .eq(WmNewsMaterial::getNewsId, dto.getId())
            );
        }
        //保存或更新文章
        WmNews wmNews = saveOrUpdateWmNews(dto);
        boolean flag = saveOrUpdate(wmNews);
        if (!flag) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (!Objects.isNull(dto.getStatus()) && dto.getStatus() == 0) {
            //是草稿，直接结束
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //不是草稿，保存文章与素材的关系
        //内容图片与素材图片的关系
        List<String> contentImageUrls = getImagesFromContent(dto.getContent());
        saveNewsAndMaterIalRelations(wmNews.getId(), contentImageUrls, WemediaConstants.WM_CONTENT_REFERENCE);
        //封面图片与素材的关系
        List<String> images = dto.getImages();
        saveNewsAndMaterIalRelations(wmNews.getId(), images, WemediaConstants.WM_COVER_REFERENCE);
        return null;
    }

    private void saveNewsAndMaterIalRelations(Integer newsId, List<String> imageUrls, Short type) {
        if (CollectionUtils.isEmpty(imageUrls)) {
            return;
        }
        //根据图片url查询素材id列表
        List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(new LambdaQueryWrapper<WmMaterial>().in(WmMaterial::getUrl, imageUrls));
        List<Integer> materialIds = wmMaterials.stream().map(t -> t.getId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialIds)) {
            return;
        }
        //保存关联关系
        int row = 0;
        try {
            row = wmNewsMaterialMapper.savaNewsMaterialRelations(newsId, materialIds, type);

        } catch (Exception e) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (row <= 0) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
    }

    private WmNews saveOrUpdateWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> imageUrls = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            imageUrls = getImagesFromContent(dto.getContent());
            if (CollectionUtils.isEmpty(imageUrls)) {
                //无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else if (imageUrls.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                //三图
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                imageUrls = imageUrls.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE).collect(Collectors.toList());
            } else {
                //单图
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                imageUrls = imageUrls.stream().limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE).collect(Collectors.toList());
            }
            dto.setImages(imageUrls);
        }
        wmNews.setImages(StringUtils.join(imageUrls, ","));
        wmNews.setEnable((short) 1);
        return wmNews;
    }

    private List<String> getImagesFromContent(String content) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (StringUtils.isBlank(content)) {
            return arrayList;
        }
        List<ContentDto> list = JSON.parseObject(content, List.class);
        List<String> imageUrls = list.stream()
                //过滤 type="image” 的数据
                .filter(t -> Objects.equals(WemediaConstants.WM_NEWS_TYPE_IMAGE, t.getType()))
                //获取 value 值
                .map(ContentDto::getValue)
                .collect(Collectors.toList());
        return imageUrls;
    }
}
