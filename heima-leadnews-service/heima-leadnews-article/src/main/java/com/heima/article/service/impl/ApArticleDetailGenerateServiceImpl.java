package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleDetailGenerateService;
import com.heima.common.constants.ArticleConstants;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.search.vos.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-11 16:50:32
 */
@Service
@Slf4j
public class ApArticleDetailGenerateServiceImpl implements ApArticleDetailGenerateService {

    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Override
    @Async
    public void generateApArticleDetailPage(ApArticle apArticle, String content) {
        String path = "";
        try {
            StringWriter out = new StringWriter();
            Template template = configuration.getTemplate("article.ftl");
            Map<String, Object> data = new HashMap<>();
            List<Map> maps = JSON.parseArray(content, Map.class);
            data.put("content", maps);
            template.process(data,out);
            //3、生成HTML，上传到Minio
            ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
            path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //4、更新ApArticle的staticUrl字段
        apArticle.setStaticUrl(path);
        apArticleMapper.updateById(apArticle);

        try {
            //发送消息到Kafka
            //组装数据
            SearchArticleVo vo = new SearchArticleVo();
            BeanUtils.copyProperties(apArticle, vo);
            vo.setContent(content);
            kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, JSON.toJSONString(vo));
        } catch (BeansException e) {
            e.printStackTrace();
        }

    }
}
