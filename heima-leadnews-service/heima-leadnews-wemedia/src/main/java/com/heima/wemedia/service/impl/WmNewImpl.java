package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.common.dtos.ContenDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WmNewImpl extends ServiceImpl<WmNewMapper, WmNews> implements WmNewService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Override
    public ResponseResult queryPage(WmNewsPageReqDto dto) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long userId = UserContext.getId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.checkParam();
        Short status = dto.getStatus();
        Date begin = dto.getBeginPubDate();
        Date end = dto.getEndPubDate();
        Integer channelId = dto.getChannelId();
        String keyword = dto.getKeyword();
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                .eq(WmNews::getUserId, userId)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .like(!Objects.isNull(keyword), WmNews::getTitle, keyword)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        // 设置查询结果到分页响应结果对象中
        pageResponseResult.setData(page.getRecords());
        // 返回分页响应结果
        return pageResponseResult;
    }

    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        Long id = UserContext.getId();
        if (Objects.isNull(id)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //如果id不为空，则删除已关联的素材关系
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
        if (Objects.isNull(dto.getStatus()) && dto.getStatus() == 0) {
            //保存的是草稿，直接结束
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //保存的不是草稿，需要进一步保存文章与素材的关系
        //内容与素材的关系
        List<String> contentImageUrls = getImagesFromContent(wmNews.getContent());
        saveNewsAndMaterialRations(wmNews.getId(), contentImageUrls, WemediaConstants.WM_CONTENT_REFERENCE);
        //图片与素材的关系
        List<String> coverImageUrls = dto.getImages();
        saveNewsAndMaterialRations(wmNews.getId(), coverImageUrls, WemediaConstants.WM_COVER_REFERENCE);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 批量保存文章与素材的关系
     *
     * @param newsId
     * @param imageUrls
     * @param type
     */
    private void saveNewsAndMaterialRations(Integer newsId, List<String> imageUrls, Short type) {
        if (CollectionUtils.isEmpty(imageUrls)) {
            return;
        }
        //根据图片URL查询素材ID列表
        List<Integer> materialIds = wmMaterialMapper
                .selectList(new LambdaQueryWrapper<WmMaterial>().in(WmMaterial::getUrl, imageUrls))
                .stream()
                .map(WmMaterial::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialIds)) {
            return;
        }
        int row = 0;
        try {
            //保存关联关系
            row = wmNewsMaterialMapper.saveNewsAndMaterialRelations(newsId, materialIds, type);
        } catch (Exception e) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        if (row == 0) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
    }

    /**
     * 从文章内容中提取图片url
     *
     * @param content
     * @return
     */
    private List<String> getImagesFromContent(String content) {
        if (StringUtils.isBlank(content)) {
            return new ArrayList<>();
        } else {
            List<ContenDto> list = JSON.parseArray(content, ContenDto.class);
            return list.stream()
                    .filter(item -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(item.getType()))
                    .map(ContenDto::getValue)
                    .collect(Collectors.toList());
        }
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

}
