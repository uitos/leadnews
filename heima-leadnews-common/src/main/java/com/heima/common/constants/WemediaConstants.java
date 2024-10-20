package com.heima.common.constants;

public interface WemediaConstants {

    Short COLLECT_MATERIAL = 1;//收藏

    Short CANCEL_COLLECT_MATERIAL = 0;//取消收藏

    String WM_NEWS_TYPE_IMAGE = "image";

    Short WM_NEWS_NONE_IMAGE = 0;  //无图
    Short WM_NEWS_SINGLE_IMAGE = 1;  //单图
    Short WM_NEWS_MANY_IMAGE = 3;  //三图
    Short WM_NEWS_TYPE_AUTO = -1;  //自动

    Short WM_CONTENT_REFERENCE = 0; //内容引用
    Short WM_COVER_REFERENCE = 1;  //封面引用

    Short WM_NEWS_STATUS_DRAFT = 0;  //草稿
    Short WM_NEWS_STATUS_SUBMIT = 1;  //提交

}