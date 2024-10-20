package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ContenDto;
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
        // 定义文章ID
        Long articleId = 1383827888816836609L;
        // 查询文章内容
        ApArticleContent articleContent = apArticleContentMapper.selectOne(
                new LambdaQueryWrapper<ApArticleContent>()
                        .eq(ApArticleContent::getArticleId, articleId)
        );
        // 获取文章内容的JSON字符串
        String json = articleContent.getContent();
        // 将JSON字符串解析为ContenDto对象列表
        List<ContenDto> contenDtos = JSON.parseArray(json, ContenDto.class);
        // 创建数据映射，用于存储文章内容
        Map<String, Object> data=new HashMap<>();
        data.put("content",contenDtos);
        // 创建字符串写入器，用于处理模板生成的内容
        StringWriter writer = new StringWriter();
        // 获取模板
        Template template = configuration.getTemplate("article.ftl");
        // 使用数据和模板生成HTML内容
        template.process(data, writer);
        // 将生成的HTML内容转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
        // 调用MinIO服务上传HTML文件
        String url = minIOFileStorageService.uploadHtmlFile("article", articleId + ".html", inputStream);
        // 创建ApArticle对象，用于更新文章的静态URL
        ApArticle apArticle = new ApArticle();
        apArticle.setId(articleId);
        apArticle.setStaticUrl(url);
        // 更新文章信息到数据库
        apArticleMapper.updateById(apArticle);
    }

}
