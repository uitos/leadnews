package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.article.dtos.ContentDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
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
                Wrappers.<ApArticleContent>lambdaQuery()
                        .eq(ApArticleContent::getArticleId, articleId)
        );
        //2、通过Freemarker填充内容
        if(apArticleContent == null || StringUtils.isBlank(apArticleContent.getContent())){
            return;
        }
        String json = apArticleContent.getContent();
        List<ContentDto> list = JSON.parseArray(json, ContentDto.class);
        HashMap<String, Object> data = new HashMap<>();
        data.put("content", list);

        StringWriter out = new StringWriter();
        Template template = configuration.getTemplate("article.ftl");
        template.process(data,out);

//        Map<String, Object> data = new HashMap<>();
        //
//        List<Map> maps = JSON.parseArray(apArticleContent.getContent(), Map.class);
//        data.put("content", maps);
//        template.process(data,out);


        //3、生成HTML，上传到Minio
        ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
        String path = minIOFileStorageService.uploadHtmlFile("", articleId + ".html", in);
        System.out.println(path);



        //4、更新ApArticle的staticUrl字段
        ApArticle apArticle = new ApArticle();
        apArticle.setId(articleId);
        apArticle.setStaticUrl(path);
        apArticleMapper.updateById(apArticle);
    }

}
