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
import org.apache.commons.lang3.StringUtils;
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
        // 一、校验参数
        if(Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        String tag = dto.getTag();
        Date minBehotTime = dto.getMinBehotTime();
        Date maxBehotTime = dto.getMaxBehotTime();
        if(Objects.isNull(size) || size <= 0 || size > 30){
            dto.setSize(10);
        }
        if(StringUtils.isBlank(tag)){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if(Objects.isNull(minBehotTime)){
            dto.setMinBehotTime(new Date());
        }
        if(Objects.isNull(maxBehotTime)){
            dto.setMaxBehotTime(new Date());
        }
        // 二、处理逻辑
        List<ApArticle> apArticleList = apArticleMapper.selectListByArticleHomeDto(dto, type);
        // 三、封装数据
        return ResponseResult.okResult(apArticleList);
    }

    @Override
    @Transactional
    public ResponseResult<Long> saveArticle(ArticleDto dto) {
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);
        //构造文章内容实体
        if (Objects.isNull(dto.getId())){
            //保存文章基本数据
            save(apArticle);
            //保存文章配置数据
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);
            //保存文章内容数据
            ApArticleContent articleContent = new ApArticleContent();
            articleContent.setArticleId(apArticle.getId());
            articleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(articleContent);
        } else {
            //更新基本数据
            updateById(apArticle);
            //更新内容数据
            ApArticleContent articleContent = new ApArticleContent();
            articleContent.setContent(dto.getContent());
            apArticleContentMapper.update(articleContent,
                    new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, dto.getId()));
        }
        return ResponseResult.okResult(apArticle.getId());
    }
}
