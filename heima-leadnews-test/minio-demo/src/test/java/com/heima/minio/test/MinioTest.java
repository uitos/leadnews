package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:03:11
 */
@SpringBootTest
public class MinioTest {

    @Resource
    private FileStorageService fileStorageService;

    @Test
    public void testUpload() throws Exception {

        FileInputStream fileInputStream = new FileInputStream("D:\\list.html");
        String pash = fileStorageService.uploadHtmlFile("", "test.html", fileInputStream);
        System.out.println(pash);

    }


}
