package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-08 10:32:05
 */
@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {

    /**
     * 批量保存
     * @param wmMaterialIds
     * @param wmNewsId
     * @param type
     */
    void saveRelations(@Param("mids") List<Integer> wmMaterialIds,
                       @Param("newsId") Integer wmNewsId,
                       @Param("type") Short type);
}
