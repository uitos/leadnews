package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleContentDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.kafka.common.protocol.types.Field;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

/**
 * @author mianbao
 */
@SpringBootTest
public class ArticleFreemarkerMinIoTest {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Test
    public void testGenerateHtml() throws Exception {

        Long articleId = 1302862387124125698L;

        //1.去数据库中查找内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
                new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, articleId)
        );
        //2.对数据进行处理，放入freemarker
        String content = apArticleContent.getContent();
        List<ArticleContentDto> list = JSON.parseArray(content, ArticleContentDto.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", list);


//        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
//                new LambdaQueryWrapper<ApArticleContent>().eq(ApArticleContent::getArticleId, articleId)
//        );
//        String content = apArticleContent.getContent();
//        List<ArticleContentDto> list = JSONObject.parseArray(content, ArticleContentDto.class);
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("content", list);
//
//        //freemarker
//        Template template = configuration.getTemplate("article.ftl");
//        StringWriter stringWriter = new StringWriter();
//        template.process(map, stringWriter);
//
//        //minIo
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stringWriter.toString().getBytes());
//        String path = fileStorageService.uploadHtmlFile("", articleId.toString(), byteArrayInputStream);
//
//        //更新表
//        ApArticle apArticle = new ApArticle();
//        apArticle.setAuthorId(articleId);
//        apArticle.setStaticUrl(path);
//        apArticleMapper.updateById(apArticle);


    }


}
