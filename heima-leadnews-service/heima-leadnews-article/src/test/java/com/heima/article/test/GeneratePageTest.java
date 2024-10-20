package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.article.dtos.ContentDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:57:44
 */
@Slf4j
@SpringBootTest
public class GeneratePageTest {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private Configuration configuration;
    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    /**
     *
     * @throws Exception
     */
    @Test
    public void generateHtmlToMinioTest() throws Exception {
        //1、查询文章内容
        Long articleId = 1302862387124125698L;
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
                new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, articleId)
        );

        //填充模板
        List<ContentDto> list = JSON.parseArray(apArticleContent.getContent(), ContentDto.class);
        Template template = configuration.getTemplate("article.ftl");
        HashMap<String, Object> data = new HashMap<>();
        data.put("content", list);
        StringWriter out = new StringWriter();
        template.process(data, out);
        //上传OSS
        ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
        String path = minIOFileStorageService.uploadHtmlFile("", articleId + ".html", in);
        //更新文章的静态url
        ApArticle article = new ApArticle();
        article.setId(articleId);
        article.setStaticUrl(path);
        int flag = apArticleMapper.updateById(article);
        log.info("更新文章静态路径结果:{}",flag);

    }

}
