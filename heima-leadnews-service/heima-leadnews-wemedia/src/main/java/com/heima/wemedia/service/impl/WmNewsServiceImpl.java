package com.heima.wemedia.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
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
import com.heima.wemedia.utils.WmThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
@Transactional
public class WmNewsServiceImpl  extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Override
    public ResponseResult findPage(WmNewsPageReqDto dto) {
        // 一.校验参数
        if(dto == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer wmUserId = WmThreadLocalUtil.getWmUserId();
        if(wmUserId == null) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.checkParam();
        // 二.处理业务
        // 分页
        IPage<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        // 条件
        Short status = dto.getStatus();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Integer channelId = dto.getChannelId();
        String keyword = dto.getKeyword();
        lambdaQuery()
                .eq(status != null, WmNews::getStatus, status)
                .between(begin != null && end != null, WmNews::getPublishTime, begin, end)
                .eq(channelId != null, WmNews::getChannelId, channelId)
                .like(StringUtils.isNotBlank(keyword), WmNews::getTitle, keyword)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        // 三.封装数据
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    @Override
    public ResponseResult submit(WmNewsDto dto) {
        // 校验数据
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 处理业务

        if(dto.getId()!= null){
            // 如果id不为空说明是更新，更新前先删除关系。
            deleteRelations(dto.getId());
        }
        //更新或者新增
        WmNews wmNews = createWmNews(dto);
        boolean flag = saveOrUpdate(wmNews);
        if(!flag) {
            throw new CustomException(AppHttpCodeEnum.NEWS_PUBLISHED_FAILED);
        }
        //3、判断是否为草稿
        if(WemediaConstants.WM_NEWS_STATUS_DRAFT.equals(dto.getStatus())){
            //是草稿
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //4、不是草稿保存文章与素材的关联关系
        //保存文章内容图片与素材的关联关系：插入数据到中间表
        List<String> contentImageUrls = getImageUrlsFromContent(dto.getContent());
        saveRelations(contentImageUrls, wmNews.getId(), WemediaConstants.WM_CONTENT_REFERENCE);
        //保存文章封面图片与素材的关联关系
        List<String> coverImageUrls = dto.getImages();
        saveRelations(coverImageUrls, wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);
        // 封装数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult deleteById(Integer id) {
        // 删除关联关系
        deleteRelations(id);
        // 删除文章
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveRelations(List<String> imageUrls, Integer wmNewsId, Short type) {
        if(CollectionUtils.isNotEmpty(imageUrls)) {
            List<WmMaterial> wmMaterialList = wmMaterialMapper.selectList(
                    Wrappers.<WmMaterial>lambdaQuery()
                            .in(WmMaterial::getUrl, imageUrls)
            );
            List<Integer> wmMaterialIds = wmMaterialList.stream().map(WmMaterial::getId).collect(Collectors.toList());
            wmNewsMaterialMapper.saveRelations(wmMaterialIds, wmNewsId, type);
        }
    }
    private WmNews createWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);
        // 补全实体
        wmNews.setUserId(WmThreadLocalUtil.getWmUserId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> images = dto.getImages();  //如果是自动  []
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            //type==-1,images就是一个[]
            images = getImageUrlsFromContent(dto.getContent());  //从内容中提取的图片集合一定不是null
            if(images.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                //当图片 >=3，选择3图。选择前3张作为封面图片
                images = images.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
            } else if(images.size() == WemediaConstants.WM_NEWS_NONE_IMAGE){
                //当图片 ==0，选择无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else {
                //否则，选择单图，选择第1张作为封面图片
                images = images.stream().limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
            }
            //布局选择自动，需要把内容中的图片设置成封面图片
            dto.setImages(images);
        }
        //StringUtils.join(集合, ",")  //把集合转成字符串，以逗号分隔
        wmNews.setImages(StringUtils.join(images, ","));
        return wmNews;
    }

    private List<String> getImageUrlsFromContent(String content) {
        if(StringUtils.isNotBlank(content)) {
            List<ContentDto> contentDtoList = JSON.parseArray(content, ContentDto.class);
            return contentDtoList.stream()
                    //过滤的是type="image"的对象数据
                    .filter(v -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(v.getType()))
                    //再加工：得到value
                    .map(ContentDto::getValue)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    private void deleteRelations(Integer wmNewsId) {
        wmNewsMaterialMapper.delete(
                Wrappers.<WmNewsMaterial>lambdaQuery()
                        .eq(WmNewsMaterial::getNewsId, wmNewsId)
        );
    }
}
