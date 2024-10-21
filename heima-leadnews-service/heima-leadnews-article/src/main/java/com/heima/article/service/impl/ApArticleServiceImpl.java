package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
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

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.heima.common.constants.ArticleConstants.DEFAULT_TAG;

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

    @Resource
    private ApArticleContentMapper apArticleContentMapper;

    @Resource
    private ApArticleConfigMapper apArticleConfigMapper;
    @Override
    public ResponseResult load(ArticleHomeDto dto,Short type) {
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        if(Objects.isNull(size) || size>30 || size<0){
            dto.setSize(10);
        }
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(DEFAULT_TAG);
        }
        if(dto.getMaxBehotTime()==null){
            dto.setMaxBehotTime(new Date());
        }
        if(dto.getMinBehotTime()==null){
            dto.setMinBehotTime(new Date());
        }
        List<ApArticle> apArticles = apArticleMapper.selectArticles(dto,type);
        return ResponseResult.okResult(apArticles);


    }

    @Override
    @Transactional
    public ResponseResult<Long> saveArticle(ArticleDto dto) {
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle=new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);

        if(Objects.isNull(dto.getId())){
            save(apArticle);
            ApArticleContent apArticleContent=new ApArticleContent(apArticle.getId(),dto.getContent());
            int insert = apArticleContentMapper.insert(apArticleContent);
            if(insert==0){
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            ApArticleConfig apArticleConfig=new ApArticleConfig(apArticle.getId());
            int insertConfig = apArticleConfigMapper.insert(apArticleConfig);
            if(insertConfig==0){
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
        }else{
            updateById(apArticle);
            ApArticleContent apArticleContent=new ApArticleContent();
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.update(apArticleContent,new LambdaQueryWrapper<ApArticleContent>()
                    .eq(ApArticleContent::getArticleId,apArticle.getId()));

        }
        return ResponseResult.okResult(apArticle.getId());
    }
}
