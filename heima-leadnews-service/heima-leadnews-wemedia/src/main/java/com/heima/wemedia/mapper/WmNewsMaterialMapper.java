package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {

    int batchInsert(@Param("newsId") Integer id,@Param("materialIds") List<Integer> materialIds,@Param("type")Short type);
}
