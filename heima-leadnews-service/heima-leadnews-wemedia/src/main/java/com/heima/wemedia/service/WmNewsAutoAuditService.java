package com.heima.wemedia.service;

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
}