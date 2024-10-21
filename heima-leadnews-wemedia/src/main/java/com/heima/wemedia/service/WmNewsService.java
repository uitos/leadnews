package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;


/**
 * @author enchanter
 */
public interface WmNewsService extends IService<WmNews> {

    /**
     * 分页条件查询
     * @param dto
     * @return
     */
    ResponseResult pageQuery(WmNewsPageReqDto dto);

    /**
     * 保存或更新
     * @param dto 封装参数
     * @return 成功或失败
     */
    ResponseResult submit(WmNewsDto dto);
}
