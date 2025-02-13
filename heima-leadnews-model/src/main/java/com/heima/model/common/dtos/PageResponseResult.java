package com.heima.model.common.dtos;

import java.io.Serializable;

/**
 * 分页响应实体
 */
public class PageResponseResult extends ResponseResult implements Serializable {
    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 页大小
     */
    private Integer size;

    /**
     * 总条数
     */
    private Integer total;

    public PageResponseResult(Integer currentPage, Integer size, Integer total) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }

    public PageResponseResult() {

    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
