package com.heima.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.FileInputStream;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:03:11
 */
@SpringBootTest
public class MinioTest {


    public static void main(String[] args) {

        FileInputStream fileInputStream = null;
        try {
            //目标文件
            fileInputStream =  new FileInputStream
                    ("F:\\leadnews-dmr-1017\\heima-leadnews\\heima-leadnews-test\\freemarker-demo\\src\\main\\resources\\templates\\list.ftl");
            //1.创建minio链接客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://192.168.200.130:9000").build();
            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("A/B/C/list.html")//文件名
                    .contentType("text/html")//文件类型
                    .bucket("dmrleadnews")//桶名词  与minio创建的名词一致
                    .stream(fileInputStream, fileInputStream.available(), -1) //文件流
                    .build();
            minioClient.putObject(putObjectArgs);
            System.out.println("http://192.168.200.130:9000/leadnews/list.html");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
