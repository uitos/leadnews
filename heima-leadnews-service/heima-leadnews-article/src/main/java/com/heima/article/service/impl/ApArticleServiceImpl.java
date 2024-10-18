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
        // 检验参数
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(dto.getSize() == null){
            dto.setSize(10);
        }
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        // 没传最大时间表示想看最新的
        if(Objects.isNull(dto.getMaxBehotTime())){
            dto.setMaxBehotTime(new Date());
        }
        if(Objects.isNull(dto.getMinBehotTime())){
            // 没传最小时间，表示想看以前的
            dto.setMinBehotTime(new Date());
        }
        // 处理逻辑
        List<ApArticle> apArticleList = apArticleMapper.selectList(dto,type);

        // 封装数据
        return ResponseResult.okResult(apArticleList);
    }
}
