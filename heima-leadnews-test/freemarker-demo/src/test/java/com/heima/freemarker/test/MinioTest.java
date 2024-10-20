package com.heima.freemarker.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest
public class MinioTest {
    @Test
    public void UploadHtmlTest() throws Exception {
        FileInputStream in = new FileInputStream("E:\\temp\\demo.html");
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://192.168.200.130:9000")
                        .credentials("minio", "minio123")
                        .build();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("leadnews")
                        .object("demo.html")
                        .stream(in, in.available(), -1)
                        .contentType("text/html")
                        .build());

    }
}
