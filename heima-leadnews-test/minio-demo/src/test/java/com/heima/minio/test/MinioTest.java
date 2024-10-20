package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest
public class MinioTest {
    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void uploadTest() throws Exception {
        FileInputStream in = new FileInputStream("G:\\ghy\\test.html");
        String s = fileStorageService.uploadHtmlFile("", "testtt.html", in);
        System.out.println(s);
    }
}
