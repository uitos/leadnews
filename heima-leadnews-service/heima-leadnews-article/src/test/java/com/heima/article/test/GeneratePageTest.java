package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.article.dtos.contentDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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

    @Resource
    private Configuration configuration;

    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private ApArticleMapper apArticleMapper;

    @Resource
    private ApArticleContentMapper apArticleContentMapper;


    /**
     * @throws Exception
     */
    @Test
    public void generateHtmlToMinioTest() throws Exception {
//        //1.从数据库中查询文章数据
//        Long articleId = 1383827952326987778L;
//        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
//                new LambdaQueryWrapper<ApArticleContent>()
//                        .eq(ApArticleContent::getArticleId, articleId));
//
//        //填充模板数据
//        String json = apArticleContent.getContent();
//        List<contentDto> list = JSON.parseArray(json, contentDto.class);
//        Map<String, Object> data= new HashMap<>();
//        data.put("content", list);
//        StringWriter out = new StringWriter();
//        Template template = configuration.getTemplate("article.ftl");
//        template.process(data, out);
//
//        //上传到minio
//        ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
//        String pash = fileStorageService.uploadHtmlFile("", articleId + ".html", in);
//
//        //4.更新到文章表的静态URL字段
//        ApArticle apArticle = new ApArticle();
//        apArticle.setId(articleId);
//        apArticle.setStaticUrl(pash);
//        apArticleMapper.updateById(apArticle);

        //查询文章数据

        apArticleContentMapper.selectOne(
                new LambdaQueryWrapper<ApArticleContent>()
                        .eq(ApArticleContent::getArticleId, 1383827952326987778L));
    }
}
