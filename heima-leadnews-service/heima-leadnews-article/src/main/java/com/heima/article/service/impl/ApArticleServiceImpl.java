package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.service.ApArticleContentService;
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

import java.sql.Wrapper;
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

    @Override
    public ResponseResult load(ArticleHomeDto dto,Short type) {
        // 检验参数
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(dto.getSize() == null){
            dto.setSize(10);
        }
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        // 没传最大时间表示想看最新的
        if(Objects.isNull(dto.getMaxBehotTime())){
            dto.setMaxBehotTime(new Date());
        }
        if(Objects.isNull(dto.getMinBehotTime())){
            // 没传最小时间，表示想看以前的
            dto.setMinBehotTime(new Date());
        }
        // 处理逻辑
        List<ApArticle> apArticleList = apArticleMapper.selectList(dto,type);

        // 封装数据
        return ResponseResult.okResult(apArticleList);
    }

    @Override
    public ResponseResult saveApArticle(ArticleDto dto) {
        //1.检查参数
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);
        //2.判断是否纯在id
        if(Objects.isNull(dto.getId())){
            //2.1 不纯在id 保存 文章， 文章配置 ，文章内容
            //保存文章
            save(apArticle);

            //保存配置
            //构造器初始化，设置默认值
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);

            //保存内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);
        }else {
            //2.2 存在id 修改 文章表，文章内容
            //修改 文章
            updateById(apArticle);
            //修改 文章内容
            //查询对应id的文章内容
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(
                    Wrappers.<ApArticleContent>lambdaQuery()
                            .eq(ApArticleContent::getArticleId, dto.getId()));
            //更新新的文章内容
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }
        //3.结果返回 文章id
        return ResponseResult.okResult(apArticle.getId());
    }
}
