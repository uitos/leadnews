package com.heima.search.service.impl;

import com.heima.common.exception.CustomException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.pojos.ApAssociateWords;
import com.heima.search.service.ApAssociateWordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;


/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-15 17:19:31
 */
@Service
@Slf4j
public class ApAssociateWordsServiceImpl implements ApAssociateWordsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult search(UserSearchDto userSearchDto) {
        if(userSearchDto == null || StringUtils.isBlank(userSearchDto.getSearchWords())) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(userSearchDto.getPageSize() <= 0 || userSearchDto.getPageSize() > 10){
            userSearchDto.setPageSize(10);
        }
        Query query = Query.query(
                Criteria.where("associateWords")
                        .regex(Pattern.compile("^.*" + userSearchDto.getSearchWords() + ".*$", Pattern.CASE_INSENSITIVE)))
                .with(Sort.by(Sort.Direction.DESC, "createdTime"))
                .limit(userSearchDto.getPageSize());
        List<ApAssociateWords> apAssociateWordsList = mongoTemplate.find(query, ApAssociateWords.class);
        return ResponseResult.okResult(apAssociateWordsList);
    }
}
