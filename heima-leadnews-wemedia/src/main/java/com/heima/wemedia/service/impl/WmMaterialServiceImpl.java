//package com.heima.wemedia.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.heima.model.common.dtos.ResponseResult;
//import com.heima.model.wemedia.pojos.WmMaterial;
//import com.heima.wemedia.mapper.WmMaterialMapper;
//import com.heima.wemedia.service.WmMaterialService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//import java.util.function.Function;
//
///**
// * @author enchanter
// */
//@Service
//@Transactional
//@Slf4j
//public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
//
////@Autowired
////private FilestoreService filestoreService;
//
//    @Override
//    public ResponseResult uploadPicture(MultipartFile file) {
//        //1.校验参数
////        if(Objects.isNull( ))
//        return null;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public boolean saveBatch(Collection<WmMaterial> entityList, int batchSize) {
//        return false;
//    }
//
//    @Override
//    public boolean saveOrUpdateBatch(Collection<WmMaterial> entityList, int batchSize) {
//        return false;
//    }
//
//    @Override
//    public boolean updateBatchById(Collection<WmMaterial> entityList, int batchSize) {
//        return false;
//    }
//
//    @Override
//    public boolean saveOrUpdate(WmMaterial entity) {
//        return false;
//    }
//
//    @Override
//    public WmMaterial getOne(Wrapper<WmMaterial> queryWrapper, boolean throwEx) {
//        return null;
//    }
//
//    @Override
//    public Map<String, Object> getMap(Wrapper<WmMaterial> queryWrapper) {
//        return Collections.emptyMap();
//    }
//
//    @Override
//    public <V> V getObj(Wrapper<WmMaterial> queryWrapper, Function<? super Object, V> mapper) {
//        return null;
//    }
//
//    @Override
//    public BaseMapper<WmMaterial> getBaseMapper() {
//        return null;
//    }
//
//    @Override
//    public Class<WmMaterial> getEntityClass() {
//        return null;
//    }
//
//
//}