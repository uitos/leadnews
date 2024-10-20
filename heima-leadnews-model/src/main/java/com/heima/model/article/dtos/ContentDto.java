package com.heima.model.article.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 11:18:15
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
@Builder   //构建者设计模式
public class ContentDto {

    private String type;
    private String value;

}
