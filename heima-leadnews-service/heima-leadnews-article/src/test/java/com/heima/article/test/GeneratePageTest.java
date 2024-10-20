package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ContentDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:57:44
 */
@SpringBootTest
public class GeneratePageTest {

    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApArticleMapper apArticleMapper;

    /**
     *
     * @throws Exception
     */
    @Test
    public void generateArticleDetailPageTest() throws Exception {
        //1.查询文章内容
        Long articleId = 1383827952326987778L;
        ApArticleContent articleContent = apArticleContentMapper.selectOne(
                new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, articleId));
        //2.填充模板
        String json = articleContent.getContent();
        List<ContentDto> list = JSON.parseArray(json, ContentDto.class);
        Map<String, Object> data = new HashMap<>();
        data.put("content", list);
        StringWriter out = new StringWriter();
        Template template = configuration.getTemplate("article.ftl");
        template.process(data, out);
        //3.上传到OSS
        ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
        String path = fileStorageService.uploadHtmlFile("", articleId + ".html", in);
        //4.更新文章的静态URL
        ApArticle article = new ApArticle();
        article.setId(articleId);
        article.setStaticUrl(path);
        apArticleMapper.updateById(article);
    }

}
