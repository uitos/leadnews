package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wm.pojos.WmMaterial;
import com.heima.model.wm.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {


    void saveRelations(@Param("wmMaterials")List<WmMaterial> wmMaterials,@Param("newsId") Integer newsId);

}