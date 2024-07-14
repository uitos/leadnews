package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-14 17:43:58
 */
public interface ArticleSearchService {

    /**
     * 搜索框搜索
     * @param dto
     * @return
     */
    ResponseResult search(@RequestBody UserSearchDto dto) throws Exception;

}
