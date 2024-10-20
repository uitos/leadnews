package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import com.heima.file.service.impl.MinIOFileStorageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    FileStorageService fileStorageService;

    @Test
    public void uploadTest() throws Exception {
        FileInputStream in = new FileInputStream("D:\\list.html");

        String s = fileStorageService.uploadHtmlFile("", "testDemo.html", in);
        System.out.println(s);


    }
}
