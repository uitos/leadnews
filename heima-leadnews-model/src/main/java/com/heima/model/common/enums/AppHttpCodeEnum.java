package com.heima.model.common.enums;

/**
 * 定义异常的错误信息
 */
public enum AppHttpCodeEnum {

    // 成功段0
    SUCCESS(200,"操作成功"),
    // 登录段1~50
    NEED_LOGIN(1,"需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2,"密码错误"),
    PHONE_OR_PASSWORD_ERROR(3,"手机号或密码错误"),
    // TOKEN50~100
    TOKEN_INVALID(50,"无效的TOKEN"),
    TOKEN_EXPIRE(51,"TOKEN已过期"),
    TOKEN_REQUIRE(52,"TOKEN是必须的"),
    // SIGN验签 100~120
    SIGN_INVALID(100,"无效的SIGN"),
    SIG_TIMEOUT(101,"SIGN已过期"),
    // 增删改失败
    INSERT_FAIL(110,"增加失败"),
    UPDATE_FAIL(111,"更新失败"),
    DELETE_FAIL(112,"删除失败"),

    // 参数错误 500~1000
    PARAM_REQUIRE(500,"缺少参数"),
    PARAM_INVALID(501,"无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502,"图片格式有误"),
    SERVER_ERROR(503,"服务器内部错误"),
    // 数据错误 1000~2000
    DATA_EXIST(1000,"数据已经存在"),
    AP_USER_DATA_NOT_EXIST(1001,"ApUser数据不存在"),
    DATA_NOT_EXIST(1002,"数据不存在"),
    FILE_UPLOAD_ERROR(1003,"文件上传错误"),
    WM_MATERIAL_DATA_NOT_EXIST(1004,"素材不存在"),
    NEWS_PUBLISHED_FAILED(1005,"文章发布失败"),
    AP_ARTICLE_INSERT_FAIL(1006,"APP端文章增加失败"),
    AP_ARTICLE_CONFIG_INSERT_FAIL(1007,"APP端文章配置增加失败"),
    AP_ARTICLE_CONTENT_INSERT_FAIL(1008,"APP端文章内容增加失败"),
    AP_ARTICLE_UPDATE_FAIL(1009,"APP端文章更新失败"),
    AP_ARTICLE_CONTENT_UPDATE_FAIL(1010,"APP端文章内容更新失败"),
    WM_NEWS_DATA_NOT_EXIST(1011,"自媒体端文章不存在"),
    WM_NEWS_STATUS_NOT_SUBMIT(1012,"自媒体端文章状态不是待审核"),
    TEXT_AUDIT_EX(1013,"文本审核异常"),
    IMAGE_AUDIT_EX(1013,"图片审核异常"),



    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000,"无权限操作"),
    NEED_ADMIN(3001,"需要管理员权限");

    int code;
    String errorMessage;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
