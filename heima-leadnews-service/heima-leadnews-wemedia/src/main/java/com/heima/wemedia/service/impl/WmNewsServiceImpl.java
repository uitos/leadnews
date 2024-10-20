package com.heima.wemedia.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.heima.common.exception.CustomException;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wm.dtos.WmNewsDto;
import com.heima.model.wm.dtos.WmNewsPageReqDto;
import com.heima.model.wm.pojos.WmMaterial;
import com.heima.model.wm.pojos.WmNews;
import com.heima.model.wm.pojos.WmNewsMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.utils.WmUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {


    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 多条件查询文章列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findAllByStatus(WmNewsPageReqDto dto) {
        // 一.校验参数
        if (dto == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        // 二.处理业务
        // 分页
        IPage<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        // 条件
        lambdaQuery()
                .eq(dto.getStatus() != null, WmNews::getStatus, dto.getStatus())
                .between(dto.getBeginPubDate() != null && dto.getEndPubDate() != null, WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate())
                .eq(dto.getChannelId() != null, WmNews::getChannelId, dto.getChannelId())
                .like(StringUtils.isNotEmpty(dto.getKeyword()), WmNews::getTitle, dto.getKeyword())
                .eq(WmNews::getUserId, WmUserContext.getUserId())
                .orderByDesc(WmNews::getCreatedTime)
                .page(page);

        // 三.封装数据
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    /**
     * 提交文章初稿
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        //参数校验
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (dto.getId() != null) {
            //将草稿删除
            wmNewsMapper.deleteById(dto.getId());
            //2、删除wm_news_material表中的数据
            wmNewsMaterialMapper.delete(new QueryWrapper<WmNewsMaterial>().lambda()
                    .eq(WmNewsMaterial::getNewsId, dto.getId()));
        }
        //数据处理
        //status 0 为草稿 1为待审核
        //1、提取草稿数据存入news表中
        WmNews wmNews = new WmNews();
        Long userId = WmUserContext.getUserId();
        wmNews.setTitle(dto.getTitle());
        wmNews.setContent(dto.getContent());
        wmNews.setChannelId(dto.getChannelId());
        wmNews.setType(dto.getType());
        wmNews.setStatus(dto.getStatus());
        wmNews.setUserId(userId);
        wmNews.setLabels(dto.getLabels());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setPublishTime(dto.getPublishTime());
        List<String> images = dto.getImages();
        //图片用逗号分隔
        StringBuffer imageStr = new StringBuffer();
        for (int i = 0; i < images.size(); i++) {
            if (i == images.size() - 1) {
                imageStr.append(images.get(i));
            } else {
                imageStr.append(images.get(i) + ",");
            }
        }
        wmNews.setImages(imageStr.toString());
        wmNewsMapper.insert(wmNews);
        log.info("文章id:{}", wmNews.getId());
        //2、通过素材url获取 wmMaterial中的素材id、type存入wm_news_material表中
        List<WmMaterial> wmMaterials = wmMaterialMapper.selectBatchUrls(images);
        for (int i = 0; i < wmMaterials.size(); i++) {
            WmMaterial wmMaterial = wmMaterials.get(i);
            //插入wm_news_material表中的数据
            wmNewsMaterialMapper.
                    saveRelations(wmMaterial.getId(), wmMaterial.getType(), wmNews.getId(), i);
        }

        return new ResponseResult();
    }

    /**
     * 根据文章id查询文章详情
     *
     * @param newsId
     * @return
     */
    @Override
    public ResponseResult selectByNewsId(Integer newsId) {
        // 校验参数
        if (newsId == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = wmNewsMapper.selectById(newsId);
        return ResponseResult.okResult(wmNews);
    }

    /**
     * 上下架文章
     * @param dto
     * @return
     */
    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = new WmNews();
        wmNews.setId(dto.getId());
        wmNews.setEnable(dto.getEnable());
        int i = wmNewsMapper.updateById(wmNews);
        if (i < 1) {
            throw new CustomException(AppHttpCodeEnum.DOWN_OR_UP_CONTENTS_ERROR);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 删除文章
     * @param newsId 文章id
     * @return
     */
    @Override
    @Transactional
    public ResponseResult deleteByNewsId(Integer newsId) {
        if(newsId == null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        //删除文章的同时 wm_news_material表中的数据也需要删除
        int i = wmNewsMapper.deleteById(newsId);
        wmNewsMaterialMapper.deleteByNewsId(newsId);
        if(i>=1){
            return ResponseResult.okResult(200, "删除文章成功");
        }
        throw new CustomException(AppHttpCodeEnum.DELETE_CONTENTS_ERROR);

    }
}