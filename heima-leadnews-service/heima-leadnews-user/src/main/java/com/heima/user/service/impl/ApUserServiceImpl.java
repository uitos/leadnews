package com.heima.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-03 11:23:56
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    @Override
    public ResponseResult login(LoginDto dto) {
        //一、校验参数
        if(dto == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        //二、处理业务
        Map<String, Object> data = new HashMap<>();
        String phone = dto.getPhone();
        String password = dto.getPassword();
        // 1.判断是否为登录
        if(StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(password)) {
            // 2.通过用户名查询数据库
            ApUser userDB = this.lambdaQuery().eq(ApUser::getPhone, phone).one();
            // 3.用户是否存在
            if(userDB == null) {
                throw new CustomException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
            }
            // 4.比对密码
            password = MD5Utils.encodeWithSalt(password, userDB.getSalt());
            if(!password.equals(userDB.getPassword())) {
                throw new CustomException(AppHttpCodeEnum.PHONE_OR_PASSWORD_ERROR);
            }
            //5.判断状态
            if(!userDB.getStatus()){
                throw new CustomException(AppHttpCodeEnum.USER_STATUS_ERROR);
            }
            //三、封装数据
            //成功
            data.put("token", AppJwtUtil.getToken(userDB.getId().longValue()));
            //数据脱敏
            userDB.setPassword("");
            userDB.setSalt("");
            data.put("user",userDB);
            return ResponseResult.okResult(data);
        }
        //三、封装数据
        //不登录，先看看
        data.put("token", AppJwtUtil.getToken(0L));
        return ResponseResult.okResult(data);
    }
}
