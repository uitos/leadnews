package com.heima.search.utils;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 11:46:06
 */
public class AppThreadLocalUtil {

    public static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置APP用户ID到当前线程
     * @param appUserId
     */
    public static void setAppUserId(Integer appUserId){
        THREAD_LOCAL.set(appUserId);
    }

    /**
     * 从当前线程上获取APP用户ID
     * @return
     */
    public static Integer getAppUserId(){
        return THREAD_LOCAL.get();
    }

    /**
     * 从当前线程上移除APP用户ID
     * @return
     */
    public static void removeAppUserId(){
        THREAD_LOCAL.remove();
    }

}
