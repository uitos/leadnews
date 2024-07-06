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

    /**
     *
     * @throws Exception
     */
    @Test
    public void uploadTest() throws Exception {
        //1.创建minio对应的Java客户端
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://192.168.200.130:9000")
                        .credentials("minio", "minio123")
                        .build();
        //2.构造上传对象
        FileInputStream in = new FileInputStream("D:\\list.html");
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket("test")
                .object("pages/list.html")
                .stream(in, in.available(), -1)
                .contentType("text/html")
                .build();
        //3.上传
        minioClient.putObject(putObjectArgs);
    }


    @Autowired
    private FileStorageService fileStorageService;

    /**
     *
     * @throws Exception
     */
    @Test
    public void starterFileUploadTest() throws Exception {
        String url = fileStorageService.uploadHtmlFile("", "mylist.html", new FileInputStream("D:\\list.html"));
        System.out.println(url);
    }


}
