package com.heima.model.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 分页请求实体
 */
@Data
@Slf4j
public class PageRequestDto {

    /**
     * 页大小
     */
    protected Integer size;

    /**
     * 当前页码
     */
    protected Integer page;

    /**
     * 分页参数校验方法
     */
    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }
}
