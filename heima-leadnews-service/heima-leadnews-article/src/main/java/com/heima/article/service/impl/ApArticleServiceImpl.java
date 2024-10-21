package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleConfigService;
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
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {


    private final ApArticleMapper apArticleMapper;


    private final ApArticleConfigMapper apArticleConfigMapper;

    private final ApArticleContentMapper apArticleContentMapper;

    /**
     * 加载文章列表
     *
     * @param dto  文章列表参数
     * @param type 文章类型
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //1、参数校验
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        if (Objects.isNull(size) || size > 10 || size <= 0 || size > 30) {
            dto.setSize(size);
        }
        if (Objects.isNull(dto.getTag())) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if (Objects.isNull(dto.getMaxBehotTime())) {
            dto.setMaxBehotTime(new Date());
        }
        if (Objects.isNull(dto.getMinBehotTime())) {
            dto.setMinBehotTime(new Date());
        }
        //2、查询文章列表
        List<ApArticle> list = apArticleMapper.selectListByArticleHomeDto(dto, type);
        //3、封装返回结果
        return ResponseResult.okResult(list);

    }

    /**
     * 保存文章信息
     *
     * @param dto 文章信息
     * @return 保存成功的文章id
     */
    @Override
    public ResponseResult<Long> saveArticle(ArticleDto dto) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);
        if (Objects.isNull(dto.getId())) {
            save(apArticle);

            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);
            ApArticleContent articleContent = new ApArticleContent();
            articleContent.setArticleId(apArticle.getId());
            articleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(articleContent);
        } else {
            updateById(apArticle);
            ApArticleContent articleContent = new ApArticleContent();
            articleContent.setContent(dto.getContent());
            apArticleContentMapper.update(articleContent,
                    new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, dto.getId()));
        }
         return ResponseResult.okResult(apArticle.getId());
        }
    }
