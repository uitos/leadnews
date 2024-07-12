package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.article.IArticleClient;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.common.tess4j.Tess4jClient;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.ContentDto;
import com.heima.model.wemedia.dtos.ContentImagesDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoAuditService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-09 08:49:49
 */
@Service
@Slf4j
public class WmNewsAutoAuditServiceImpl implements WmNewsAutoAuditService {

    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private Tess4jClient tess4jClient;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    //@Transactional(rollbackFor = Exception.class)
    @GlobalTransactional   //表示全局事务的入口
    @Async   //该方法异步执行
    public void autoAuditWmNews(Integer id) {
        try {
            log.warn("自动审核当前线程:{}", Thread.currentThread().getName());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.warn("ID为{}文章自动审核开始了", id );
        // 1.查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if(wmNews == null) {
            throw new CustomException(AppHttpCodeEnum.WM_NEWS_DATA_NOT_EXIST);
        }
        if(WmNews.Status.SUBMIT.getCode() != wmNews.getStatus()) {
            //状态不是待审核
            throw new CustomException(AppHttpCodeEnum.WM_NEWS_STATUS_NOT_SUBMIT);
        }
        // 2.提取文本与图片
        ContentImagesDto contentImagesDto = getTextAndImages(wmNews);
        //敏感词过滤
        boolean flag = sensitiveFilter(contentImagesDto.getContent(), wmNews);
        //审核结果， true:成功   false:失败
        if(!flag) {
            return;
        }
        //TODO 将来要用，松开就好=======================
        // 3. 审核文本【内容文本、标题、标签】 审核不成功，则更新WmNews
        flag = auditText(contentImagesDto.getContent(), wmNews);
        //审核结果， true:成功   false:失败
        if(!flag) {
            return;
        }
        // 4.审核图片【封面、内容图片】
        flag = auditImages(contentImagesDto.getImages(), wmNews);
        //审核结果， true:成功   false:失败
        if(!flag) {
            return;
        }
        //========================
        // 5.判断是否到达发布时间
        if(wmNews.getPublishTime().getTime() < System.currentTimeMillis()) {
            // 5.到了，发布，同步数据到APP端
            Long articleId = saveApArticle(wmNews);
            //制造异常：演示分布式事务问题
            //int i = 1/0;
            if(articleId > 0){
                wmNews.setArticleId(articleId);
                updateWmNews(wmNews, WmNews.Status.PUBLISHED.getCode(), "");
            } else {
                log.warn("自媒体微服务：收到文章微服务进入降级了---->");
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_INSERT_FAIL);
            }
        } else {
            // 6.没到，修改wm_news表数据  状态：审核通过    理由：审核通过
            updateWmNews(wmNews, WmNews.Status.SUCCESS.getCode(), "审核通过");
           /* //使用Redisson实现延迟队列
            //向延迟队列中增加一个任务
            RBlockingDeque<Integer> blockingDeque = redissonClient.getBlockingDeque("article-deque");
            RDelayedQueue<Integer> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            long time = wmNews.getPublishTime().getTime() - System.currentTimeMillis();
            delayedQueue.offer(wmNews.getId(), time, TimeUnit.MILLISECONDS);
            */
            //使用RabbitMQ的死信队列+TTL实现延迟发布文章
            long time = wmNews.getPublishTime().getTime() - System.currentTimeMillis();
            Message message = MessageBuilder
                    .withBody(wmNews.getId().toString().getBytes(StandardCharsets.UTF_8))
                    .setExpiration(time + "")
                    .build();
            amqpTemplate.convertAndSend("article.publish.direct.exchange", "article.delay", message);
        }
    }

    /**
     * 敏感词过滤
     */
    private boolean sensitiveFilter(String content, WmNews wmNews) {
        //1、初始化Map
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(null);
        Set<String> words = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toSet());
        SensitiveWordUtil.initMap(words);
        //2、过滤
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if(map.size() > 0) {
            //3、返回结果
            log.warn("文章中有敏感词:{}", map);
            updateWmNews(wmNews,WmNews.Status.FAIL.getCode(),map.toString());
            return false;
        }
        //3、返回结果
        return true;
    }

    /**
     * 通过OpenFeign去远程调用APP端保存文章的业务
     * @param wmNews
     * @return
     */
    @Override
    public Long saveApArticle(WmNews wmNews) {
        //1.补全实体
        ArticleDto dto = new ArticleDto();
        BeanUtils.copyProperties(wmNews, dto, "id");
        dto.setContent(wmNews.getContent());
        Integer userId = wmNews.getUserId();
        WmUser wmUser = wmUserMapper.selectById(userId);
        if(wmUser != null) {
            dto.setAuthorId(userId.longValue());
            dto.setAuthorName(wmUser.getName());
        }
        WmChannel wmChannel = wmChannelMapper.selectById(dto.getChannelId());
        if(wmChannel != null) {
            dto.setChannelName(wmChannel.getName());
        }
        dto.setLayout(wmNews.getType());
        dto.setFlag((byte) 0);
        //2.远程调用
        ResponseResult responseResult = articleClient.saveApArticle(dto);
        if(responseResult.getCode().equals(200)){
            return (Long) responseResult.getData();
        } else {
            log.warn("文章自动审核，远程调用APP端保存文章信息，出异常了:{}",responseResult.getErrorMessage());
            throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_INSERT_FAIL);
        }
    }

    private boolean auditImages(List<String> images, WmNews wmNews) {
        boolean flag = true;  //审核通过
        try {
            //去重
            images = images.stream().distinct().collect(Collectors.toList());
            if(images == null || images.size() == 0){
                return flag;
            }
            List<byte[]> imageBytes = new ArrayList<>(images.size());
            for (String url : images) {
                byte[] bytes = fileStorageService.downLoadFile(url);
                imageBytes.add(bytes);
                //提取图片中文字，进行过滤
                //把字节数组转成流对象
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                String text = tess4jClient.doOCR(image);
                flag = sensitiveFilter(text, wmNews);
                //审核结果， true:成功   false:失败
                return flag;
            }
            //审核
            /*Map map = greenImageScan.imageScan(imageBytes);
            String suggestion = map.get("suggestion").toString();
            if(AliyunAuditResultConstants.BLOCK.equals(suggestion)) {
                flag = false;
                updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), AliyunAuditResultConstants.BLOCK_MESSAGE + ":" + map.get("label"));
            } else if(AliyunAuditResultConstants.REVIEW.equals(suggestion)) {
                flag = false;
                updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), AliyunAuditResultConstants.REVIEW_MESSAGE + ":" + map.get("label"));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), AppHttpCodeEnum.IMAGE_AUDIT_EX.getErrorMessage());
            throw new CustomException(AppHttpCodeEnum.TEXT_AUDIT_EX);
        }
        return flag;
    }

    /**
     * 审核文本
     * @param content
     * @param wmNews
     * @return
     */
    private boolean auditText(String content, WmNews wmNews) {
        boolean flag = true;  //审核通过
        /*try {
            Map map = greenTextScan.greenTextScan(content);
            String suggestion = map.get("suggestion").toString();
            if(AliyunAuditResultConstants.BLOCK.equals(suggestion)) {
                flag = false;
                updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), AliyunAuditResultConstants.BLOCK_MESSAGE + ":" + map.get("label"));
            } else if(AliyunAuditResultConstants.REVIEW.equals(suggestion)) {
                flag = false;
                updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), AliyunAuditResultConstants.REVIEW_MESSAGE + ":" + map.get("label"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), AppHttpCodeEnum.TEXT_AUDIT_EX.getErrorMessage());
            throw new CustomException(AppHttpCodeEnum.TEXT_AUDIT_EX);
        }*/
        return flag;
    }

    /**
     * 更新文章的状态及审核结果信息
     * @param wmNews
     * @param status
     * @param reason
     */
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        int row = wmNewsMapper.updateById(wmNews);
        if(row <= 0){
            throw new CustomException(AppHttpCodeEnum.UPDATE_FAIL);
        }
    }

    /**
     * 提取文章的文本与内容
     * @param wmNews  文本：标题、标签、内容   图片：封面、内容
     * @return
     */
    private ContentImagesDto getTextAndImages(WmNews wmNews) {
        //放图片
        List<String> images = new ArrayList<>();
        //放文本
        StringBuffer text = new StringBuffer("");
        //获取内容中的图片与文本
        String content = wmNews.getContent();
        if(StringUtils.isNotBlank(content)) {
            List<ContentDto> contentDtoList = JSON.parseArray(content, ContentDto.class);
            for (ContentDto contentDto : contentDtoList) {
                if (WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(contentDto.getType())) {
                    //把提取到的内容图片放到集合中
                    images.add(contentDto.getValue());
                } else {
                    text.append(contentDto.getValue()).append(" - ");
                }
            }
        }
        //其他文本
        text.append(wmNews.getTitle()).append(" - ").append(wmNews.getLabels());
        //封面图片
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] coverImageArray = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(coverImageArray));
        }
        ContentImagesDto dto = new ContentImagesDto();
        dto.setImages(images);
        dto.setContent(text.toString());
        return dto;
    }

}
