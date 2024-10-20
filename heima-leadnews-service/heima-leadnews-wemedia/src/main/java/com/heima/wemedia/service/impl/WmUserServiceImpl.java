package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wm.dtos.WmLoginDto;
import com.heima.model.wm.pojos.WmUser;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {


    @Override
    public ResponseResult login(WmLoginDto dto) {
        //1.检查参数
        if(StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"用户名或密码为空");
        }

        //2.查询用户
        WmUser wmUser = getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, dto.getName()));
        if(wmUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.WM_USER_DATA_NOT_EXIST);
        }

        //3.比对密码
        String salt = wmUser.getSalt();
        String pswd = dto.getPassword();
        pswd = MD5Utils.encodeWithSalt(pswd, salt);
        if(pswd.equals(wmUser.getPassword())){
            //4.返回数据  jwt
            Map<String,Object> map  = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(wmUser.getId().longValue()));
            wmUser.setSalt("");
            wmUser.setPassword("");
            map.put("user",wmUser);
            return ResponseResult.okResult(map);

        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.encodeWithSalt("admin","123456"));
    }
}