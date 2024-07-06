package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.ApArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
