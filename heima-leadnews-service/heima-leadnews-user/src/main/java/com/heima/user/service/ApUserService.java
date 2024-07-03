package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;

/**
 * 用户业务接口
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-03 11:23:15
 */
public interface ApUserService extends IService<ApUser> {

    /**
     * 登录
     * @param dto 手机和密码
     * @return
     */
    ResponseResult login(LoginDto dto);
}
