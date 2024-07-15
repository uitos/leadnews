package com.heima.search.service.impl;

import com.heima.search.pojos.ApUserSearch;
import com.heima.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-15 14:59:33
 */
@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");


    @Override
    @Async
    public void save(String keyword, Integer userId) {
        if(StringUtils.isBlank(keyword) || userId == null) {
            log.error("非法参数");
            return;
        }
        Query query = Query.query(Criteria.where("keyword").is(keyword).and("userId").is(userId));
        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        if(apUserSearch != null){
            //apUserSearch.setCreatedTime(SDF.format(new Date()));
            apUserSearch.setCreatedTime(new Date());
            //ID相同会覆盖
            mongoTemplate.save(apUserSearch);
            return;
        }
        //不存在，肯定得保存一条新记录
        ApUserSearch userSearch = new ApUserSearch();
        userSearch.setUserId(userId);
        userSearch.setKeyword(keyword);
        //userSearch.setCreatedTime(SDF.format(new Date()));
        userSearch.setCreatedTime(new Date());
        Query query2 = Query.query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "createdTime"));
        List<ApUserSearch> apUserSearchList = mongoTemplate.find(query2, ApUserSearch.class);
        if(apUserSearchList.size() < 10) {
            //没到10，直接保存
            mongoTemplate.save(userSearch);
        } else {
            //到10条，替换最旧的那条
            ApUserSearch oldApUserSearch = apUserSearchList.get(apUserSearchList.size() - 1);
            Query query3 = Query.query(Criteria.where("id").is(oldApUserSearch.getId()));
            mongoTemplate.findAndReplace(query3, userSearch);
        }
    }
}
