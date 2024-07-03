package com.heima.common.exception;

import com.heima.model.common.enums.AppHttpCodeEnum;

/**
 * 自定义异常
 * @author ghy
 */
public class CustomException extends RuntimeException {

    /**
     * 异常信息
     */
    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return appHttpCodeEnum;
    }
}
