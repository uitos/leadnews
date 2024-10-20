package com.heima.common.utils;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 15:00:44
 */
public class UserContext {

    public static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 把ID绑定到当前线程
     * @param id 用户ID
     */
    public static void setId(Long id){
        THREAD_LOCAL.set(id);
    }

    /**
     * 获取当前线程上的ID
     * @return
     */
    public static Long getId(){
        return THREAD_LOCAL.get();
    }

    /**
     * 从当前线程上移除绑定的ID
     */
    public static void removeId(){
        THREAD_LOCAL.remove();
    }

}
