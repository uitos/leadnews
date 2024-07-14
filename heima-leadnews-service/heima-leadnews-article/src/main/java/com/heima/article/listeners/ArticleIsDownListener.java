package com.heima.article.listeners;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.common.constants.WmNewsMessageConstants;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.wemedia.dtos.WmNewsEnableDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-14 15:09:37
 */
@Component
@Slf4j
public class ArticleIsDownListener {

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @KafkaListener(topics=WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC)
    public void handleMessage(String json){
        log.warn("收到的消息:{}",json);
        if(StringUtils.isNotBlank(json)) {
            WmNewsEnableDto dto = JSON.parseObject(json,WmNewsEnableDto.class);
            ApArticleConfig apArticleConfig = new ApArticleConfig();
            apArticleConfig.setIsDown(dto.getEnable() != 1);
            apArticleConfigMapper.update(apArticleConfig,
                    Wrappers.<ApArticleConfig>lambdaQuery().eq(ApArticleConfig::getArticleId, dto.getId()));
        }
    }

}
