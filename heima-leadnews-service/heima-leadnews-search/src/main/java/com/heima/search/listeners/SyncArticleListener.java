package com.heima.search.listeners;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.search.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-15 10:26:47
 */
@Component
@Slf4j
public class SyncArticleListener {

    @Autowired
    private RestHighLevelClient client;

    @KafkaListener(topics=ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
    public void handleMessage(String message) {
        try {
            if(StringUtils.isNotBlank(message)) {
                SearchArticleVo vo = JSON.parseObject(message, SearchArticleVo.class);
                IndexRequest request = new IndexRequest("app_info_article");
                request.id(vo.getId().toString())
                                .source(message, XContentType.JSON);
                client.index(request, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文章数据同步到ES出错了:{}", e.getMessage());
        }
    }

}