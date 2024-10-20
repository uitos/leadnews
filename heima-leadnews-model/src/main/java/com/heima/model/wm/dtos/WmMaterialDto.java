package com.heima.model.wm.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
    private Integer page;
    private Integer size;

    /**
     * 分页参数校验方法
     */
    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 7 || this.size > 100) {
            setSize(10);
        }
    }
}
