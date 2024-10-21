package com.heima.wemedia.test;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-21 15:44:55
 */
@SpringBootTest
public class AliyunGreenTest {

    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     *
     * @throws Exception
     */
    @Test
    public void textScanTest() throws Exception {
        String content = "我是一段安全的文本，冰毒";
        Map map = greenTextScan.greenTextScan(content);
        System.out.println(map);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void imageScanTest() throws Exception {
        String url = "http://192.168.200.130:9000/leadnews/20241021155141468.jpg";
        byte[] bytes = fileStorageService.downLoadFile(url);
        List<byte[]> list = new ArrayList<>();
        list.add(bytes);
        Map map = greenImageScan.imageScan(list);
        System.out.println(map);
    }


}
