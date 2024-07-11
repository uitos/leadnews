package com.heima.article.service;

import com.heima.article.feign.ArticleClient;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.pojos.ApArticle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-08 17:29:15
 */
@SpringBootTest
public class ArticleClientTest {

    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Test
    public void testSaveApArticle(){
        Long articleId = 1302977754114826241L;
        ApArticle apArticle = apArticleMapper.selectById(articleId);
        apArticle.setId(null);
        apArticle.setAuthorName("itheima");
        ArticleDto dto = new ArticleDto();
        BeanUtils.copyProperties(apArticle, dto);
        dto.setContent("[{\"type\":\"text\",\"value\":\"杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl892wKAZLhtAASZUi49De0836.jpg\"},{\"type\":\"text\",\"value\":\"杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸杨澜回应一秒变脸\"},{\"type\":\"text\",\"value\":\"11111111\"}]");
        articleClient.saveApArticle(dto);
    }

}