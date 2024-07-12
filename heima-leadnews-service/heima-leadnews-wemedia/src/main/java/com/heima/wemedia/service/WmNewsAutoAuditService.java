package com.heima.wemedia.service;

import com.heima.model.wemedia.pojos.WmNews;

/**
 * 文章自动审核
 * @author ghy
 */
public interface WmNewsAutoAuditService {

    /**
     * 自媒体文章审核
     * @param id  自媒体文章id
     */
    void autoAuditWmNews(Integer id);

    /**
     * 同步文章到APP端
     * @param wmNews
     * @return
     */
    Long saveApArticle(WmNews wmNews);
}