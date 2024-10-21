package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;


/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:03:11
 */
@Slf4j
@SpringBootTest
public class MinioTest {

    /**
     * MinIO上传文件
     *
     * @throws Exception
     */
    @Test
    public void uploadHtmlTest() throws Exception {
        FileInputStream inputStream = new FileInputStream("/Users/aaronlee/Downloads/test.html");
        //构建上传对象
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://121.36.199.82:9000/")
                        .credentials("minio", "minio123")
                        .build();
        //文件上传
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("leadnews")
                        .object("demoObject.html")
                        .stream(inputStream, inputStream.available(), -1)   //.available()计算字节数
                        .contentType("text/html")   //MinIO中的tomcat识别html文件
                        .build());
    }

    @Autowired
    FileStorageService fileStorageService;

    @Test
    public void fileUploadtest() throws Exception{
        FileInputStream inputStream = new FileInputStream("/Users/aaronlee/Downloads/test.html");
        String path = fileStorageService.uploadHtmlFile("", "demoObject2.html", inputStream);
        log.info("自定义封装上传URL路径：{}",path);
    }


}
