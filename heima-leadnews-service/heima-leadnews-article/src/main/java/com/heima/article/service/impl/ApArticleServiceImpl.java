package com.heima.article.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.net.nntp.Article;
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
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public ResponseResult getByArticleHomeDto(ArticleHomeDto dto, Short type) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        if (Objects.isNull(size) || size <= 0 || size > 30) {
            dto.setSize(10);
        }
        String tag = dto.getTag();
        if (StringUtils.isBlank(tag)) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        Date maxBehotTime = dto.getMaxBehotTime();
        if (Objects.isNull(maxBehotTime)) {
            dto.setMaxBehotTime(new Date());
        }
        Date minBehotTime = dto.getMinBehotTime();
        if (Objects.isNull(minBehotTime)) {
            dto.setMinBehotTime(new Date());
        }
        List<ApArticle> apArticles = apArticleMapper.selectList(dto, null);

        return ResponseResult.okResult(apArticles);
    }
}

