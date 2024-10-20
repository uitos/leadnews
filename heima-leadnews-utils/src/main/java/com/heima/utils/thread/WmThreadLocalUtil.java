package com.heima.utils.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author enchanter
 */
@Slf4j
public class WmThreadLocalUtil {
    public static final ThreadLocal<Integer> WM_USER_THREAD_LOCAL =new ThreadLocal<>();
    public static void  setUserId(Integer userId){
        WM_USER_THREAD_LOCAL.set(userId);
    }
    public static Integer getUserId(){
        return WM_USER_THREAD_LOCAL.get();
    }
    public static void removeUserId(){
        WM_USER_THREAD_LOCAL.remove();
    }
}
