package com.heima.search.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-15 15:14:34
 */
@SpringBootTest
public class ApUserSearchServiceTest {

    @Autowired
    private ApUserSearchService apUserSearchService;

    @Test
    public void save() {
        apUserSearchService.save("测试搜索72",1);
    }
}