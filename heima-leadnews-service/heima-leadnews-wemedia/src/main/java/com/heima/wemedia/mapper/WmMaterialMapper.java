package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mianbao
 * 1
 */
@Mapper
public interface WmMaterialMapper extends BaseMapper<WmMaterial> {

    List<Integer> selectIds(@Param("urls") List<String> images);
}
