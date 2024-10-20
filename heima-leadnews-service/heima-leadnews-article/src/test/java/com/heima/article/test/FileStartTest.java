package com.heima.article.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.common.constants.ArticleConstants;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootTest
public class FileStartTest {


    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleContentMapper articleContentMapper;
    @Resource
    private ApArticleMapper articleMapper;


    @Test
    public void testUploadHtml() throws FileNotFoundException {
        FileInputStream fileInput = new FileInputStream("F:\\leadnews-dmr-1017\\heima-leadnews\\heima-leadnews-test\\freemarker-demo\\src\\main\\resources\\templates\\basic.ftl");
        fileStorageService.uploadHtmlFile("dmrTest", "basic.html", fileInput);
    }


    @Test
    @Transactional
    void generatorHtmlTest() throws IOException, TemplateException {
        //1383828014629179393
        Long id = 1383828014629179393L;
        //先通过文章id获取文章内容
        QueryWrapper<ApArticleContent> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ApArticleContent::getArticleId, id);
        ApArticleContent apArticleContent = articleContentMapper.selectOne(wrapper);
        //判断获取的对象和对象中的文章内容是否为空
        if (apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())) {
            Template template = configuration.getTemplate("article.ftl");
            //把内容写入模板页面中
            StringWriter out = new StringWriter();
            HashMap<String, Object> dataModel = new HashMap<>();
            dataModel.put("content", JSON.parseArray(apArticleContent.getContent()));
            template.process(dataModel, out);

            //再将模板页面写入到minio中
            //创建输出流
            ByteArrayInputStream in = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
            //上传成功后获取服务器上的文件访问地址
            String url = fileStorageService.uploadHtmlFile("", id + ".html", in);
            //更新文章的static_url路径
            ApArticle apArticle = new ApArticle();
            apArticle.setId(id);
            apArticle.setStaticUrl(url);
            int i = articleMapper.updateById(apArticle);
            System.out.println(i >= 1 ? "更新成功" : "更新失败");
        }
    }
}
