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
import java.util.Map;

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
     * @throws Exception
     */
    @Test
    public void generateHtmlToMinioTest() throws Exception {
        //apArticleContent.getContent() 文章内容在数据库中的结构：JSON数组
/**
 * [
 *     {
 *         "type": "text",
 *         "value": ""
 *     },
 *     {
 *         "type": "image",
 *         "value": "http://192.168.200.130/group1/M00/00/00/wKjIgl9V2CqAZe18AAOoOOsvWPc041.png"
 *     },
 *     {
 *         "type": "text",
 *         "value": ""
 *     },
 *     {
 *         "type": "text",
 *         "value": ""
 *     }
 * ]
 */
        //1、查询文章内容
        Long articleId = 1302862387124125698L;
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
                Wrappers.<ApArticleContent>lambdaQuery()
                        .eq(ApArticleContent::getArticleId, articleId)
        );
        //2、通过Freemarker填充内容
        if (apArticleContent == null || StringUtils.isBlank(apArticleContent.getContent())) {
            return;
        }
        //用于捕获字符串输出
        StringWriter out = new StringWriter();
        //获取魔板对象
        Template template = configuration.getTemplate("article.ftl");
        Map<String, Object> data = new HashMap<>();
        List<ContentDto> maps = JSON.parseArray(apArticleContent.getContent(), ContentDto.class);
        data.put("content", maps);
        template.process(data, out);
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
