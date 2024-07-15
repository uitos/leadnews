package com.heima.search.service;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-15 14:57:10
 */
public interface ApUserSearchService {

    /**
     * 保存用户搜索记录
     * @param keyword
     * @param userId
     */
    void save(String keyword, Integer userId);

}
