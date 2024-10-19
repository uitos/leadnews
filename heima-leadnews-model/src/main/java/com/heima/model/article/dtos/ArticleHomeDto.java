package com.heima.model.article.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // 最大时间
    private Date maxBehotTime;
    // 最小时间
    private Date minBehotTime;
    // 分页size
    private Integer size = 10;
    // 频道ID
    private String tag;

}