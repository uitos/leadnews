package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.ContentDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
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

/**
 * @author mianbao
 * 1
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

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


    @Transactional
    @Override
    public ResponseResult submit(WmNewsDto dto) {
        //检查dto参数是否为null
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //删除关联信息
        wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>().eq(WmNewsMaterial::getNewsId, dto.getId()));
        //获取文章封面类型
        Short type = dto.getType();
        List<String> imagesList = new ArrayList<>();
        //如果文章封面类型为自动，从文章内容中获取图片
        if (Objects.equals(type, WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            String content = dto.getContent();
            if (Objects.nonNull(content)) {
                List<ContentDto> contentDtos = JSON.parseArray(content, ContentDto.class);
                imagesList = contentDtos.stream()
                        .filter(item -> Objects.equals(item.getType(), WemediaConstants.WM_NEWS_TYPE_IMAGE))
                        .map(ContentDto::getValue)
                        .limit(3)
                        .collect(Collectors.toList());
                dto.setImages(imagesList);
                //如果封面类型为自动，传入的type是-1，需要手动矫正
                if (CollectionUtils.isEmpty(imagesList)) {
                    dto.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
                }
                if (imagesList.size() == WemediaConstants.WM_NEWS_SINGLE_IMAGE) {
                    dto.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                }
                if (imagesList.size() > WemediaConstants.WM_NEWS_SINGLE_IMAGE) {
                    dto.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                }
            }
        }
        //将dto转为pojo插入数据库
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        wmNews.setEnable((short) 1);
        wmNews.setUserId(UserContext.getUserId().intValue());
        wmNews.setCreatedTime(new Date());
        boolean save = saveOrUpdate(wmNews);
        if (!save) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        //如果是草稿不走这里，直接结束，如果是提交，则获取dto的封面图片id，如果图片id为空，则同样无需插入数据，不走这里
        if (WemediaConstants.WM_NEWS_SUBMIT.equals(dto.getStatus()) && !CollectionUtils.isEmpty(dto.getImages())) {
            List<WmMaterial> relationMaterial = wmMaterialMapper.selectList(new LambdaQueryWrapper<WmMaterial>().in(WmMaterial::getUrl, dto.getImages()));
            List<Integer> ids = relationMaterial.stream().map(WmMaterial::getId).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(ids)) {
                Integer rows = wmNewsMaterialMapper.insertMany(ids, wmNews);
                if (rows != ids.size()) {
                    throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
                }
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}
