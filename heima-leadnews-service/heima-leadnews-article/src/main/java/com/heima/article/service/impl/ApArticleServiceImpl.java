package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleDetailGenerateService;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.vos.ApArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
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
        // 二.处理业务
        // 查询条件 频道、状态：已发布【配置表中不下架、不删除】、降序：发布时间
        // 两表连查
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

}
