package com.heima.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import sun.corba.Bridge;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 09:03:11
 */
@SpringBootTest
@Slf4j
public class MinioTest {

    /**
     * 把list.html文件上传到minio中，并且可以在浏览器中访问
     *
     * @param args
     */
    public static void main(String[] args){

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("D://list.html");
            //1.获取minio的链接信息，创建一个minio的客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://192.168.200.130:9000")
                    .build();

            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("list.html") //文件名词
                    .contentType("text/html") //文件类型
                    .bucket("leadnews") //桶的名称 与minio管理创建的桶一致即可界面
                    .stream(fileInputStream,fileInputStream.available(),-1)
                    .build();
            minioClient.putObject(putObjectArgs);

            //访问路径
            System.out.println("http://192.168.200.130:9000/leadnews/list.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
