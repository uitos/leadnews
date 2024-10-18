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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public ResponseResult listByArticleHomeDto(ArticleHomeDto dto,Short type) {
        //1.判断条件
        if (dto==null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        String tag = dto.getTag();
        Integer size = dto.getSize();
        Date minBehotTime = dto.getMinBehotTime();
        Date maxBehotTime = dto.getMaxBehotTime();
        if (size==0 || size >30){
            dto.setSize(10);
        }
        if (tag==null){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if (maxBehotTime==null){
            dto.setMaxBehotTime(new Date());
        }
        if (minBehotTime==null){
            dto.setMinBehotTime(new Date());
        }

        //2.处理业务
        List<ApArticle> list = apArticleMapper.selectListByArticleHome(dto,type);
        //封装数据
        return ResponseResult.okResult(list);
    }
}
