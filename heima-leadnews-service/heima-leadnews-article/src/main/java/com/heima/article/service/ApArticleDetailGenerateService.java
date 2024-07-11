package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-11 16:48:38
 */
public interface ApArticleDetailGenerateService {

    /**
     * 生成App端文章详情页
     * @param apArticle 文本基本数据
     * @param content 内容
     */
    void generateApArticleDetailPage(ApArticle apArticle, String content);

}
