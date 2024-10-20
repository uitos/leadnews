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
    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testUpload() throws Exception {
        FileInputStream in = new FileInputStream("D:\\list.html");
        String url = fileStorageService.uploadHtmlFile("   ", "lycTestBlankList.html", in);
        System.out.println(url);

    }
}
