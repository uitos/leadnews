package com.heima.wemedia.utils;

public class WmUserContext {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到ThreadLocal
     * @param userId 用户id
     */
    public static void setUserId(Long userId) {
        THREAD_LOCAL.set(userId);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户id
     */
    public static Long getUserId() {
        return THREAD_LOCAL.get();
    }

    /**
     * 移除当前登录用户信息
     */
    public static void removeUser(){
        THREAD_LOCAL.remove();
    }
}