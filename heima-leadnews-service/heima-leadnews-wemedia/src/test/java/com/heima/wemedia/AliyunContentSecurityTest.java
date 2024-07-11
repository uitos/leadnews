package com.heima.wemedia;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Map;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-08 16:14:57
 */
@SpringBootTest
public class AliyunContentSecurityTest {

    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 文本审核
     * @throws Exception
     */
    @Test   //org.junit.jupiter.api.Test
    public void greenTextScanTest() throws Exception {
        String content = "我在上海出差，讲Java！！我不卖冰毒！";
        Map map = greenTextScan.greenTextScan(content);
        System.out.println(map);
    }

    /**
     * 图片审核
     * @throws Exception
     */
    @Test
    public void greenImageScanTest() throws Exception {
        //String url = "http://192.168.200.130:9000/leadnews/2024/07/06/9594f71e429d423eb06ed28c029560f1.jpg";
        String url = "http://192.168.200.130:9000/leadnews/20240708162358625.jpg";
        byte[] bytes = fileStorageService.downLoadFile(url);
        Map map = greenImageScan.imageScan(Arrays.asList(bytes));
        System.out.println(map);
    }

}
