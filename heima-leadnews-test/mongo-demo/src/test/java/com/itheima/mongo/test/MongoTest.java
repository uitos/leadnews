package com.itheima.mongo.test;


import com.itheima.mongo.MongoApplication;
import com.itheima.mongo.pojo.ApAssociateWords;
import com.itheima.mongo.pojo.ApUserSearch;
import com.mongodb.client.result.DeleteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = MongoApplication.class)
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @throws Exception
     */
    @Test
    public void Test() throws Exception {
        Query query = Query.query(Criteria.where("keyword").is("测试搜索4").and("userId").is(1));
        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        System.out.println(apUserSearch);
    }

    //保存
    @Test
    public void saveTest(){
        ApAssociateWords apAssociateWords = new ApAssociateWords();
        apAssociateWords.setAssociateWords("测试");
        apAssociateWords.setCreatedTime(new Date());
        ApAssociateWords words = mongoTemplate.save(apAssociateWords);
        System.out.println(words);
    }

    //根据ID查询一个
    @Test
    public void saveFindById(){
        ApAssociateWords associateWords = mongoTemplate.findById("669499a234d38e625fafb701", ApAssociateWords.class);
        System.out.println(associateWords);
    }

    //根据条件查询一个
    @Test
    public void saveFindOne(){
        Query query = Query.query(
                Criteria.where("associateWords").is("测试"));
        ApAssociateWords associateWords = mongoTemplate.findOne(query, ApAssociateWords.class);
        System.out.println(associateWords);
    }

    //条件查询
    @Test
    public void testQuery(){
        Query query = Query.query(
                Criteria.where("createdTime")
                        .gt(java.sql.Date.valueOf("2020-01-01"))
                        .and("associateWords").in("测试")
        );
        List<ApAssociateWords> associateWordsList = mongoTemplate.find(query, ApAssociateWords.class);
        System.out.println(associateWordsList.size());
    }

    @Test
    public void testDel(){
        Query query = Query.query(
                Criteria.where("associateWords").is("测试"));
        DeleteResult deleteResult = mongoTemplate.remove(query, ApAssociateWords.class);
        System.out.println(deleteResult);
    }
}
