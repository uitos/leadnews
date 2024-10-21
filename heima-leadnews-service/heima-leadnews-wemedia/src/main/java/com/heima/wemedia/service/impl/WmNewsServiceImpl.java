package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
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
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
        lambdaQuery().eq(WmNews::getUserId, userId)
                .eq(!Objects.isNull(status), WmNews::getStatus, status)
                .like(StringUtils.isNotEmpty(keyword), WmNews::getTitle, keyword)
                .eq(!Objects.isNull(channelId), WmNews::getChannelId, channelId)
                .between(!Objects.isNull(begin) && !Objects.isNull(end), WmNews::getPublishTime, begin, end)
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);

        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }


    @Override
    public ResponseResult submit(WmNewsDto dto) {
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (!Objects.isNull(dto.getId())) {
            // 更新，删除关联关系
            wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>()
                    .eq(WmNewsMaterial::getNewsId, dto.getId()));
        }
        // 保存或更新文章
        WmNews wmNews = createWmNews(dto);
        boolean flag = saveOrUpdate(wmNews);  // Table 注入存在更新记录，否者插入一条记录
        // 判断是否修改陈成功
        if (!flag) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
        if (!Objects.isNull(dto.getStatus()) && dto.getStatus() == 0) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        // 不是草稿，保存文章与素材的关系
        // 内容与素材的关系
        List<String> contentImageUrls = getImagesFromconten(wmNews.getContent());
        saveWmNewsMaterialRelations(wmNews.getId(), contentImageUrls, WemediaConstants.WM_CONTENT_REFERENCE);
        // 图片与素材的关系
        List<String> coverImageUrls = dto.getImages();
        saveWmNewsMaterialRelations(wmNews.getId(), coverImageUrls, WemediaConstants.WM_COVER_REFERENCE);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    private WmNews createWmNews(WmNewsDto dto) {
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        wmNews.setUserId(UserContext.getId().intValue());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        List<String> imageUrls = dto.getImages();
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            // 自动
            imageUrls = getImagesFromconten(dto.getContent());
            if (CollectionUtils.isEmpty(imageUrls)) {
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            } else if (imageUrls.size() >= WemediaConstants.WM_NEWS_MANY_IMAGE) {
                // 三图
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                // 通过流 截取前三张
                imageUrls.stream().limit(WemediaConstants.WM_NEWS_MANY_IMAGE).collect(Collectors.toList());
            } else {
                // 单图
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                // 通过流 截取一张
                imageUrls.stream().limit(WemediaConstants.WM_NEWS_SINGLE_IMAGE).collect(Collectors.toList());
            }
            dto.setImages(imageUrls);
        }
        wmNews.setImages(org.apache.commons.lang3.StringUtils.join(imageUrls, ","));
        wmNews.setEnable((short) 1);
        return wmNews;
    }

    private List<String> getImagesFromconten(String content) {
        if (StringUtils.isEmpty(content)) {
            return new ArrayList<>();
        } else {
            // 将文章内容中的图片地址提取出来,转换为list
            List<ContentDto> list = JSON.parseArray(content, ContentDto.class);
            return list.stream()
                    // 过滤 type="image" 的数据
                    .filter(item -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(item.getType()))
                    // 获取 value 值
                    .map(ContentDto::getValue)
                    .collect(Collectors.toList());
        }
    }

    private void saveWmNewsMaterialRelations(Integer newsId, List<String> imageUrls, Short type) {
        if(CollectionUtils.isEmpty(imageUrls)){
            return ;
        }
        // 根据图片URL查询素材ID列表
        List<Integer> materialIds = wmMaterialMapper
                .selectList(new LambdaQueryWrapper<WmMaterial>().in(WmMaterial::getUrl, imageUrls))
                .stream()
                .map(WmMaterial::getId)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(materialIds)){
            return;
        }
        int row=0;
        try {
           row= wmNewsMaterialMapper.saveNewsAndMaterialReations(newsId,materialIds,type);
        } catch (Exception e) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
        if(row==0){
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }
}
