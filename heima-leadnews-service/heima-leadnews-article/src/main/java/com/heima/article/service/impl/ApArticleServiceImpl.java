package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
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
        //一、校验参数
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        String tag = dto.getTag();
        Date maxBehotTime = dto.getMaxBehotTime();
        Date minBehotTime = dto.getMinBehotTime();
        if (Objects.isNull(dto) || size<0 || size>30) {
            dto.setSize(10);
        }
        if (StringUtils.isBlank(tag)) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if (Objects.isNull(maxBehotTime)){
            dto.setMaxBehotTime(new Date());
        }
        if (Objects.isNull(minBehotTime)){
            dto.setMaxBehotTime(new Date());
        }

        //二、处理逻辑
        List<ApArticle> articleHomeList = apArticleMapper.selectListByArticleHomeDto(dto,type);

        //三、封装数据
        return ResponseResult.okResult(articleHomeList);
    }
}
