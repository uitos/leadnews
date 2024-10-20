package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wm.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {


    void saveRelations(@Param("materialId")Integer materialId,
                       @Param("type") Short type,
                       @Param("newsId") Integer newsId,
                       @Param("order") Integer order);

    Integer selectIdByMaterialId(Integer materialId);

    void deleteByNewsId(Integer newsId);

}