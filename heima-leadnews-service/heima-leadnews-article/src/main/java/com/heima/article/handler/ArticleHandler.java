package com.heima.article.handler;

import com.heima.article.service.ApArticleService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-17 15:13:10
 */
@Component
@Slf4j
public class ArticleHandler {

    @Autowired
    private ApArticleService apArticleService;

    @XxlJob("computeHotArticleJobHandler")
    public void computeHotArticleJobHandler(){
        log.warn("computeHotArticleJobHandler任务定时计算热点文章开始执行了");
        apArticleService.computeHotArticle();
    }

}
