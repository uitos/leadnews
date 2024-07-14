package com.heima.model.wemedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-14 14:58:19
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
@Builder   //构建者设计模式
public class WmNewsEnableDto {

    /**
     * 自媒体文章ID
     */
    private Long id;

    /**
     * 上下架
     *  上架1  下架0
     */
    private Short enable;

}
