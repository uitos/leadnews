package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章信息表，存储已发布的文章 Mapper 接口
 * </p>
 *
 * @author ghy
 * @since 2023-09-21
 */
@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    /**
     * 查询文章
     * @param dto
     * @return
     */
    List<ApArticle> selectList(@Param("dto") ArticleHomeDto dto, @Param("type") Short type);

    /**
     * 加载文章列表
     * @param dto 封装参数
     * @return 文
     */
    List<ApArticle> load(@Param("dto") ArticleHomeDto dto);



}
