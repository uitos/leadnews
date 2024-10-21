package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章和素材关联Mapper
 *
 * @Name WmNewsMaterialMapper
 * @Author viktor
 * @Date 2024-10-21 11:48
 */
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {

    /**
     * 批量保存文章和素材关联
     * @param newsId
     * @param materialIds
     * @param type
     * @return
     */
    int saveNewsAndMaterialRelations(@Param("newsId") Integer newsId,
                                     @Param("materialIds") List<Integer> materialIds,
                                     @Param("type") Short type);
}
