package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

/**
 * @author ghy
 * @version 1.0.1
 */
@Slf4j
@SpringBootTest
public class MinioTest {

    @Autowired
    private FileStorageService fileStorageService;


    @Test
    public void testUpload() {
        try {
            FileInputStream in = new FileInputStream("/Users/mianbao/Documents/freemarker/func.html");
            String path = fileStorageService.uploadHtmlFile("", "testDemo.html", in);
            log.info("path:{}", path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
