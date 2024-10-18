package com.heima.common.utils;

/**
 * @author mianbao
 */
public class UserContext {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setId(Long id) {
        THREAD_LOCAL.set(id);
    }

    public static Long getUserId() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
