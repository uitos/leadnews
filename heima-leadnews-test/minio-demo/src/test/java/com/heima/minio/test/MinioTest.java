package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import com.heima.file.service.impl.MinIOFileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:03:11
 */
@SpringBootTest
public class MinioTest {

    @Resource
    private MinIOFileStorageService minIOFileStorageService;
    @Resource
    private FileStorageService fileStorageService;

    @Test
    public void updateHtmlFile(){
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\F\\黑马头条-课堂\\02-自媒体端开发\\demo.html");
            String filePath = minIOFileStorageService.uploadHtmlFile("", "demo1.html", fileInputStream);
            System.out.println(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
