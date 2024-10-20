package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
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
    private FileStorageService fileStorageService;

    @Test
    public void testUpload() throws FileNotFoundException {
        String path = "D:\\list.html";
        FileInputStream fileInputStream = new FileInputStream(path);
        String name = "list.html";
        String url = fileStorageService.uploadHtmlFile("", name, fileInputStream);
        System.out.println(url);
    }

}
