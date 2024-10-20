package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
