package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author mianbao
 */
@Data
public class ContentDto {

    private String type;

    private List<String> image;

    private String value;
}
