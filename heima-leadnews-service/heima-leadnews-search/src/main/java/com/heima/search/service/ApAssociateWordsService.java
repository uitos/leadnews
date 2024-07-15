package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-15 17:18:58
 */
public interface ApAssociateWordsService {

    ResponseResult search(@RequestBody UserSearchDto userSearchDto);

}
