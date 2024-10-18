package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleDetailGenerateService;
import com.heima.article.service.ApArticleService;
import com.heima.common.cache.CacheService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.vos.ApArticleVo;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmChannel;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务实现类
 * </p>
 *
 * @author ghy
 * @since 2023-09-21
 */
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApArticleDetailGenerateService apArticleDetailGenerateService;
    @Autowired
    private IWemediaClient wemediaClient;
    @Autowired
    private CacheService cacheService;

    /**
     * 从缓存中获取各频道首页数据
     * @param dto
     * @return
     */
    private ResponseResult loadFormRedis(String channel) {
        //从缓存中拿
        String json = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + channel);
        List<HotArticleVo> hotArticleVos = JSON.parseArray(json, HotArticleVo.class);
        log.warn("redis 从缓冲中获取数据");
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type, Boolean isFirstPage) {
        //没有
        // 一.校验参数
        if(dto == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (dto.getSize() == null || dto.getSize() <= 0) {
            dto.setSize(10);
        }
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if(dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }
        if(dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if(!ArticleConstants.LOADTYPE_LOAD_MORE.equals(type) && !ArticleConstants.LOADTYPE_LOAD_NEW.equals(type)) {
            type = ArticleConstants.LOADTYPE_LOAD_NEW;
        }

        /*if(isFirstPage) {
            return loadFormRedis(dto.getTag());
        }*/

        // 二.处理业务
        // 查询条件 频道、状态：已发布【配置表中不下架、不删除】、降序：发布时间
        // 两表连查
        log.warn("db 从数据库中获取数据");
        List<ApArticle> list = apArticleMapper.selectList(dto, type);
        List<ApArticleVo> voList = new ArrayList<>(list.size());
        for (ApArticle apArticle : list) {
            ApArticleVo vo = new ApArticleVo();
            BeanUtils.copyProperties(apArticle, vo);
            vo.setId(apArticle.getId().toString());
            voList.add(vo);
        }
        // 三.封装数据
        return ResponseResult.okResult(voList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveApArticle(ArticleDto dto) {
        /*try {
            //休眠三秒，模拟保存文章数据，需要执行的时长
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        // 一.校验参数
        if(dto == null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 二.处理业务
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);
        // 1.判断是保存还是修改
        if(dto.getId() == null) {
            // 2.保存
            // 保存文章基本信息
            boolean flag = this.save(apArticle);
            if(!flag) {
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_INSERT_FAIL);
            }
            //制造异常
            //int i = 1/0;
            // 保存文章配置信息
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            int row = apArticleConfigMapper.insert(apArticleConfig);
            if(row <= 0) {
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_CONFIG_INSERT_FAIL);
            }
            // 保存文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            row = apArticleContentMapper.insert(apArticleContent);
            if(row <= 0) {
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_CONTENT_INSERT_FAIL);
            }
        } else {
            // 3.修改
            // 修改文章基本信息
            boolean flag = this.updateById(apArticle);
            if(!flag) {
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_UPDATE_FAIL);
            }
            // 修改文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(dto.getId());
            apArticleContent.setContent(dto.getContent());
            int row = apArticleContentMapper.update(apArticleContent,
                    Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId())
            );//update ap_article_content set content = ? where article_id = ?
            if(row <= 0) {
                throw new CustomException(AppHttpCodeEnum.AP_ARTICLE_CONTENT_INSERT_FAIL);
            }
        }

        //APP文章发布成功。生成文章详情页
        apArticleDetailGenerateService.generateApArticleDetailPage(apArticle, dto.getContent());
        // 三.封装数据
        // 返回APP端文章的ID
        return ResponseResult.okResult(apArticle.getId());
    }

    @Override
    public void computeHotArticle() {
        //1、查询指定时间的文章
        //todo 5天前没有很多数据，所以现在写5年前，实际上线应该是5天前
        Date day = DateTime.now().minusYears(5).toDate();
        List<ApArticle> articleList = apArticleMapper.findListBeforeDate(day);
        //2、计算分值
        List<HotArticleVo> voList = computeHotArticleVo(articleList);
        //3、排序并放入缓存
        sortAndCacheToRedis(voList);
    }

    private void sortAndCacheToRedis(List<HotArticleVo> voList) {
        ResponseResult<List<WmChannel>> responseResult = wemediaClient.channels();
        if(responseResult.getCode().equals(200)) {
            List<WmChannel> channels = responseResult.getData();
            //1、得到每个频道下的文章
            for (WmChannel channel : channels) {
                //当前频道
                List<HotArticleVo> channelVoList = voList.stream().filter(v -> v.getChannelId().equals(channel.getId())).collect(Collectors.toList());
                //2、按score给文章排序
                //3、取前30条放到Redis中
                sortAndCache(channelVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + channel.getId());
            }
        }
        //所有的数据也需要取前30条放到Redis
        sortAndCache(voList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }

    /**
     * 为热点文章数据排序，放入缓存
     * @param voList
     * @param redisKey
     */
    private void sortAndCache(List<HotArticleVo> voList, String redisKey) {
        List<HotArticleVo> data = voList.stream()
                //根据score倒序
                .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                //取前30条
                .limit(30)
                .collect(Collectors.toList());
        cacheService.set(redisKey, JSON.toJSONString(data));
    }

    private List<HotArticleVo> computeHotArticleVo(List<ApArticle> articleList) {
        List<HotArticleVo> voList = new ArrayList<>();
        for (ApArticle article : articleList) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article, vo);
            Integer score = computeScore(vo);
            vo.setScore(score);
            voList.add(vo);
        }
        return voList;
    }

    private Integer computeScore(HotArticleVo vo) {
        int score = 0;
        Integer views = vo.getViews();
        Integer likes = vo.getLikes();
        Integer comment = vo.getComment();
        Integer collection = vo.getCollection();
        if(views != null && views > 0) {
            score += views;
        }
        if(likes != null && likes > 0){
            score += likes * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(comment != null && comment > 0){
            score += comment * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(collection != null && collection > 0){
            score += collection * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        return score;
    }


}
