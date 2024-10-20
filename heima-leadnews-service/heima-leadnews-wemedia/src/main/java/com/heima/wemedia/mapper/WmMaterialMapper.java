package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wm.pojos.WmMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface WmMaterialMapper extends BaseMapper<WmMaterial> {

//    @Select("select url from wm_material where url in (#{images})")
    List<WmMaterial> selectBatchUrls(@Param("list")List<String> images);
}