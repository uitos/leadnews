package com.heima.wemedia.listeners;

import com.heima.common.exception.CustomException;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsAutoAuditService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 15:19:49
 */
@Component
@Slf4j
public class ArticleDelayPublishListener {

    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private WmNewsAutoAuditService wmNewsAutoAuditService;

    /**
     * 监听文章延迟发布的消息
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "article.delay.queue"),
            exchange = @Exchange(name = "article.delay.direct", type = ExchangeTypes.DIRECT),
            key = "article.delay"
    ))
    public void handleDeadMessage(String message) {
        log.warn("收到消息:" + message);
        if (StringUtils.isNotBlank(message)) {
            Integer wmNewsId = Integer.valueOf(message);
            log.warn("发布文章到APP端:{}", wmNewsId);
            WmNews wmNews = wmNewsMapper.selectById(wmNewsId);
            if (wmNews == null) {
                return;
            }
            // 5.到了，发布，同步数据到APP端
            Long articleId = wmNewsAutoAuditService.saveApArticle(wmNews);
            if (articleId > 0) {
                wmNews.setArticleId(articleId);
                wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
                wmNewsMapper.updateById(wmNews);
            } else {
                log.warn("自媒体微服务：收到文章微服务进入降级了---->");
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_INSERT_FAIL);
            }
        }
    }

}