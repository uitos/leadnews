package com.heima.model.article.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @author enchanter
 */
@Data
public class ArticleHomeDto {

    // 最大时间
    private Date maxBehotTime;
    // 最小时间
    private Date minBehotTime;
    // 分页size
//    private Integer size = 10;
    private Integer size;
    // 频道ID
    private String tag;
}