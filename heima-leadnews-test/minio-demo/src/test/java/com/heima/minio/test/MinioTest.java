package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:03:11
 */
@SpringBootTest
public class MinioTest {

    @Test
    public void uploadHtmlTest() throws Exception{
        FileInputStream in = new FileInputStream("D:\\WFW\\demo.html");
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://192.168.200.130:9000")
                        .credentials("minio", "minio123")
                        .build();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("leadnews84")
                        .object("test.html")
                        .stream(in, in.available(), -1)
                        .contentType("text/html")
                        .build());
    }

    @Autowired
    private FileStorageService fileStorageService;
    @Test
    public void fileUploadTest() throws Exception{
        FileInputStream in = new FileInputStream("D:\\WFW\\demo.html");
        String path = fileStorageService.uploadHtmlFile("", "testdemo.html", in);
        System.out.println(path);
    }
}
