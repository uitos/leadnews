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
import org.apache.commons.net.nntp.Article;
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
@SpringBootTest
public class GeneratePageTest {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     *
     * @throws Exception
     */
    @Test
    public void generateArticleDetailPageTest() throws Exception{
        Long articleId = 1383827787629252610L;
        //根据文章id查询文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
                new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, articleId)
        );
        String json = apArticleContent.getContent();
        //把数组转成list
        List<ContentDto> list = JSON.parseArray(json, ContentDto.class);
        HashMap<String, Object> data = new HashMap<>();
        data.put("content",list);
        //加载模板
        Template template = configuration.getTemplate("article.ftl");
        StringWriter out = new StringWriter();
        template.process(data,out);

        //输出流转成输入流
        ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
        String path = fileStorageService.uploadHtmlFile("", articleId + ".html", in);

        ApArticle apArticle = new ApArticle();
        apArticle.setId(articleId);
        apArticle.setStaticUrl(path);
        apArticleMapper.updateById(apArticle);
    }
}
