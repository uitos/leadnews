package com.heima.model.article.vos;

import com.heima.model.article.pojos.ApArticle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-17 11:42:42
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
@Builder   //构建者设计模式
public class HotArticleVo extends ApArticle {

    private Integer score;

}
