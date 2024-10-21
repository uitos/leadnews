package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import org.apache.tomcat.util.http.ResponseUtil;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务类
 * </p>
 *
 * @author ghy
 * @since 2023-09-21
 */
public interface ApArticleService extends IService<ApArticle> {

    /**
     * 加载文章列表
     *
     * @param dto 文章列表参数
     * @return 文章列表
     */
    ResponseResult load(ArticleHomeDto dto, Short type);

    /**
     * 保存文章
     * @param dto 文章信息
     * @return 文章ID
     */
    ResponseResult saveArticle(ArticleDto dto);


}
