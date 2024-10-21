package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务实现类
 * </p>
 *
 * @author ghy
 * @since 2023-09-21
 */
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (dto.getSize() <= 0 || dto.getSize() > 30) {
            dto.setSize(10);
        }
        if (Objects.isNull(dto.getMaxBehotTime())) {
            dto.setMaxBehotTime(new Date());
        }
        if (Objects.isNull(dto.getMinBehotTime())) {
            dto.setMaxBehotTime(new Date());
        }
        if (StringUtils.isBlank(dto.getTag())) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        List<ApArticle> list = apArticleMapper.selectListByDto(dto, type);

        return ResponseResult.okResult(list);
    }


    @Override
    @Transactional
    public ResponseResult saveArticle(ArticleDto dto) {

        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);

        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(dto.getContent());

        //是否存在文章id，不存在则进入
        if (Objects.isNull(dto.getId())) {
            //保存文章
            int insert = apArticleMapper.insert(apArticle);
            if (insert <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }

            //保存文章配置
            ApArticleConfig apArticleConfig = new ApArticleConfig();
            apArticleConfig.setArticleId(apArticle.getId());
            apArticleConfig.setIsComment(true);
            apArticleConfig.setIsDown(false);
            apArticleConfig.setIsForward(true);
            apArticleConfig.setIsDelete(false);
            int insertConfig = apArticleConfigMapper.insert(apArticleConfig);
            if (insertConfig <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }

            //保存文章内容
            int insertContent = apArticleContentMapper.insert(apArticleContent);
            if (insertContent <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }

        } else {
            //存在文章id，进入
            //修改文章
            int update = apArticleMapper
                    .update(apArticle, new LambdaQueryWrapper<ApArticle>().eq(ApArticle::getId, dto.getId()));
            if (update <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            //保存文章内容
            int insert = apArticleContentMapper.insert(apArticleContent);
            if (insert <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
        }
        return ResponseResult.okResult(apArticle.getId());
    }
}
