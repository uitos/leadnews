//package com.heima.wemedia.listeners;
//
//import com.heima.wemedia.task.DelayWmNewsIdTask;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author ghy
// * @version 1.0.1
// * @date 2024-07-12 09:25:06
// */
//@Component
//public class DelayPublishWmNewsToAppListener {
//
//    @Autowired
//    private DelayWmNewsIdTask delayWmNewsIdTask;
//
//    @PostConstruct  //Bean初始化后就立即执行这个方法
//    public void run(){
//        delayWmNewsIdTask.handleMessage();
//    }
//
//}
