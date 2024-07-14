package com.heima.es;

import com.alibaba.fastjson.JSON;
import com.heima.es.mapper.ApArticleMapper;
import com.heima.es.pojo.SearchArticleVo;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ApArticleTest {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ApArticleMapper apArticleMapper;

    /**
     * 注意：数据量的导入，如果数据量过大，需要分页导入
     * @throws Exception
     */
    @Test
    public void init() throws Exception {
        BulkRequest bulkRequest = new BulkRequest("app_info_article");
        List<SearchArticleVo> searchArticleVos = apArticleMapper.loadArticleList();
        for (SearchArticleVo vo : searchArticleVos) {
            IndexRequest indexRequest = new IndexRequest("app_info_article")
                    .id(vo.getId().toString())
                    .source(JSON.toJSONString(vo), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

}