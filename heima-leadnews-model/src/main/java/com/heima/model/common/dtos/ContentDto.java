package com.heima.model.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章内容对应实体
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-08 10:44:18
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
@Builder   //构建者设计模式
public class ContentDto {

    private String type;
    private String value;

}