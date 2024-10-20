package com.heima.wemedia.utils;

public class WmThreadLocalUtil {

    public static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置自媒体用户ID到当前线程
     * @param wmUserId
     */
    public static void setWmUserId(Integer wmUserId){
        THREAD_LOCAL.set(wmUserId);
    }

    /**
     * 从当前线程上获取自媒体用户ID
     * @return
     */
    public static Integer getWmUserId(){
        return THREAD_LOCAL.get();
    }

    /**
     * 从当前线程上移除自媒体用户ID
     * @return
     */
    public static void removeWmUserId(){
        THREAD_LOCAL.remove();
    }

}