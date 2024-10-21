package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    /**
     * 加载首页数据
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer size = dto.getSize();
        String tag = dto.getTag();
        Short loaddir = dto.getLoaddir();
        //校验参数
        if (Objects.isNull(size) || size <= 0 || size > 30) {
            dto.setSize(10);
        }
        if (StringUtils.isBlank(tag)) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if (Objects.isNull(dto.getMinBehotTime())) {
            dto.setMinBehotTime(new Date());
        }
        if (Objects.isNull(dto.getMaxBehotTime())) {
            dto.setMaxBehotTime(new Date());
        }
        if (Objects.isNull(loaddir)) {
            dto.setLoaddir((short) 1);
        }
        //查询
        List<ApArticle> apArticleList = apArticleMapper.selectListByArticleHomeDto(dto);

        return ResponseResult.okResult(apArticleList);

    }

    @Transactional
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);
        //id为空则是新增
        if (Objects.isNull(dto.getId())) {
            //保存文章
            boolean saveArticle = save(apArticle);
            if (!saveArticle) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            //保存配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());

            int insertRows = apArticleConfigMapper.insert(apArticleConfig);
            if (insertRows <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            //保存内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            insertRows = apArticleContentMapper.insert(apArticleContent);
            if (insertRows <= 0) {
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
        } else {
            boolean updateFlag = updateById(apArticle);
            if (!updateFlag) {
                throw new CustomException(AppHttpCodeEnum.UPDATE_ERROR);
            }
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(dto.getId());
            apArticleContent.setContent(dto.getContent());
            int updateRows = apArticleContentMapper
                    .update(apArticleContent,new LambdaQueryWrapper<ApArticleContent>()
                                    .eq(ApArticleContent::getArticleId, dto.getId()));
            if (updateRows <= 0) {
                throw new CustomException(AppHttpCodeEnum.UPDATE_ERROR);
            }
        }


        return ResponseResult.okResult(apArticle.getId());
    }
}
