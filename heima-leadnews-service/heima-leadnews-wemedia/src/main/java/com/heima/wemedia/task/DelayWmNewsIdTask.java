//package com.heima.wemedia.task;
//
//import com.heima.common.exception.CustomException;
//import com.heima.model.common.enums.AppHttpCodeEnum;
//import com.heima.model.wemedia.pojos.WmNews;
//import com.heima.wemedia.mapper.WmNewsMapper;
//import com.heima.wemedia.service.WmNewsAutoAuditService;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RBlockingDeque;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author ghy
// * @version 1.0.1
// * @date 2024-07-12 09:20:32
// */
//@Component
//@Slf4j
//public class DelayWmNewsIdTask {
//
//    @Autowired
//    private RedissonClient redissonClient;
//    @Autowired
//    private WmNewsMapper wmNewsMapper;
//    @Autowired
//    private WmNewsAutoAuditService wmNewsAutoAuditService;
//    @Async
//    //@GlobalTransactional
//    public void handleMessage(){
//        RBlockingDeque<Integer> blockingDeque = redissonClient.getBlockingDeque("article-deque");
//        while (true) {
//            try {
//                //从队列中获取任务，如果拿到，直接返回。如果没有，则等10秒，如果还没有，本次拿任务中断
//                Integer wmNewsId = blockingDeque.poll(10, TimeUnit.SECONDS);
//                log.error("从队列中获取到数据:{}", wmNewsId);
//                if(wmNewsId != null){
//                    log.warn("发布文章到APP端:{}", wmNewsId);
//                    WmNews wmNews = wmNewsMapper.selectById(wmNewsId);
//                    if(wmNews == null) {
//                        return;
//                    }
//                    // 5.到了，发布，同步数据到APP端
//                    Long articleId = wmNewsAutoAuditService.saveApArticle(wmNews);
//                    //制造异常：演示分布式事务问题
//                    //int i = 1/0;
//                    if(articleId > 0){
//                        wmNews.setArticleId(articleId);
//                        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
//                        wmNewsMapper.updateById(wmNews);
//                    } else {
//                        log.warn("自媒体微服务：收到文章微服务进入降级了---->");
//                        throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_INSERT_FAIL);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.warn("获取任务失败:{}",e.getMessage());
//            }
//        }
//    }
//
//}
