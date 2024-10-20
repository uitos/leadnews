package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void uploadHtmlMinioTest() throws Exception {
        FileInputStream path = new FileInputStream("D:\\Temp\\demo.html");
        String url = fileStorageService.uploadHtmlFile("", "test.html", path);
        System.out.println(url);
    }

}
