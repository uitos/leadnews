package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-21 09:36:43
 */
@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {

    /**
     * 批量保存
     * @param newsId
     * @param materialIds
     * @param type
     * @return
     */
    int saveNewsAndMaterialRelations(@Param("newsId") Integer newsId,
                                     @Param("materialIds") List<Integer> materialIds,
                                     @Param("type") Short type);
}
