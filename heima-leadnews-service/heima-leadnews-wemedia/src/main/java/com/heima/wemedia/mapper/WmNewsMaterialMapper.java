package com.heima.wemedia.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmNewsMaterialMapper  extends BaseMapper<WmNewsMaterial> {
    /**
     * 批量保存
     * @param newsId
     * @param materialIds
     * @param type
     * @return
     */
    int saveNewsandMaterialRelations(@Param("newsId") Integer newsId,
                                     @Param("materialIds")List<Integer> materialIds,
                                     @Param("type")Short type);
}
