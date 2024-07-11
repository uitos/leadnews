package com.heima.model.wemedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-09 08:56:40
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
@Builder   //构建者设计模式
public class ContentImagesDto {

    private String content;
    private List<String> images;
}
