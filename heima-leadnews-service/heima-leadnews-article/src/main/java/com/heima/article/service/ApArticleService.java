package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务类
 * </p>
 *
 * @author ghy
 * @since 2023-09-21
 */
public interface ApArticleService extends IService<ApArticle> {

    ResponseResult load(ArticleHomeDto dto,short type);
}
