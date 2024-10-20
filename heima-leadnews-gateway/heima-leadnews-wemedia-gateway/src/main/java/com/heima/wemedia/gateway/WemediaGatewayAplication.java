package com.heima.wemedia.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author enchanter
 * @author enchanter
 * @author enchanter
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WemediaGatewayAplication {

    public static void main(String[] args) {
        SpringApplication.run(WemediaGatewayAplication.class,args);
    }
}
