package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-17 11:07:40
 */
@FeignClient(name = "leadnews-wemedia")
public interface IWemediaClient {

    @GetMapping("/inner/api/v1/channel/list")
    ResponseResult<List<WmChannel>> channels();
}
