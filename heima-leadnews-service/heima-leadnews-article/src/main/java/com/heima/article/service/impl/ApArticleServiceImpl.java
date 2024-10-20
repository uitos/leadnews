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
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.tomcat.util.http.ResponseUtil;
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
        //1、参数校验
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        if (Objects.isNull(size) ||size > 10 || size <= 0 || size > 30) {
            dto.setSize(size);
        }
       if (Objects.isNull(dto.getTag())){
           dto.setTag(ArticleConstants.DEFAULT_TAG);
       }
       if (Objects.isNull(dto.getMaxBehotTime())){
           dto.setMaxBehotTime(new Date());
       }
       if (Objects.isNull(dto.getMinBehotTime())){
           dto.setMinBehotTime(new Date());
       }
        //2、查询文章列表
       List<ApArticle> list = apArticleMapper.selectListByArticleHomeDto(dto,type);
        //3、封装返回结果
        return ResponseResult.okResult(list);

    }
}
