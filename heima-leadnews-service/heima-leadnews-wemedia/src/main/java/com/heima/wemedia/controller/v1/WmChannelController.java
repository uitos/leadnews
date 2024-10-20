package com.heima.wemedia.controller.v1;


import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmMaterialService;
import com.mysql.jdbc.ReplicationMySQLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
public class WmChannelController {

    @Resource
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult channels(){
        log.info("查询所有频道");
        return wmChannelService.listQuery();

    }




}
