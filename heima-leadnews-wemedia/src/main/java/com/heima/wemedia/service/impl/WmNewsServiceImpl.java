package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.utils.thread.WmThreadLocalUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.sql.Wrapper;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author enchanter
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    private final WmNewsMaterialMapper wmNewsMaterialMapper;
    private final WmMaterialMapper wmMaterialMapper;

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

    /**
     * 发布新闻
     *
     * @param dto 封装参数
     * @return 发布结果
     */
    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        // 校验参数
        if (Objects.isNull(UserContext.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.携带文章id则为二次发布，否则为首次发布
        if (!Objects.isNull(dto.getId())) {
            //二次发布,删除原文章与素材的关系
            delelteRelationsById(dto.getId());
        }

        //2.创建文章
        WmNews wmNews = createWnMews(dto);
        boolean flag = saveOrUpdate(wmNews);
        if (!flag) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }

        if (!Objects.isNull(dto.getStatus()) && dto.getStatus() == 0){
            //草稿状态下不生成文章内容图片与素材关系
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //状态 提交为1  草稿为0
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        List<String> contentImages = getImagesFromContent(dto.getContent());
        log.error("图片列表：{}", contentImages);
        log.error("封面图片：{}", dto.getImages());

        //关联文章内容图片与素材关系
        saveRelations(contentImages, wmNews.getId(), WemediaConstants.WM_CONTENT_REFERENCE);

        saveRelations(dto.getImages(), wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveRelations(List<String> images, Integer wmNewId, Short type) {
        if (CollectionUtils.isNotEmpty(images)) {
            List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery()
                    .in(WmMaterial::getUrl, images));
            List<Integer> imageIds = wmMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());
            if (imageIds.size() != images.size()) {
                throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_FAIL);
            }
            wmNewsMaterialMapper.saveRelations(imageIds, wmNewId, type);
        }
    }

    private List<String> getImagesFromContent(String content) {
        if (StringUtils.isBlank(content)) {
            List<Map> maps = JSON.parseArray(content, Map.class);
            return maps.stream()
                    .filter(m -> "image".equals(m.get("type")))
                    .map(m -> (String) m.get("value")).collect(Collectors.toList());

        }
        return new ArrayList<>();
    }

    /**
     * 创建文章
     *
     * @param dto 封装参数
     * @return 文章
     */
    private WmNews createWnMews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        //获取当前线程的用户id
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> images = dto.getImages();
        if (dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            if (images == null || images.size() == WemediaConstants.WM_NEWS_NONE_IMAGE) {
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else if (images.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
            } else {
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = images.stream().limit(1).collect(Collectors.toList());
            }
            //自动处理封面
            dto.setImages(images);
        }
        wmNews.setImages(StringUtils.join(images, ","));
        return wmNews;
    }

    /**
     * 删除文章与素材的关系
     *
     * @param id 文章id
     */
    private void delelteRelationsById(Integer id) {
        wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId, id));
    }
}
