//package com.heima.wemedia.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.heima.model.common.dtos.ResponseResult;
//import com.heima.wemedia.service.MaterialService;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//import java.util.function.Function;
//
///**
// * @author enchanter
// */
//public class MaterialServiceImpl implements MaterialService {
//    @Override
//    public ResponseResult uploadPicture(MultipartFile file) {
//
//    }
//
//    @Override
//    public boolean saveBatch(Collection<MultipartFile> entityList, int batchSize) {
//        return false;
//    }
//
//    @Override
//    public boolean saveOrUpdateBatch(Collection<MultipartFile> entityList, int batchSize) {
//        return false;
//    }
//
//    @Override
//    public boolean updateBatchById(Collection<MultipartFile> entityList, int batchSize) {
//        return false;
//    }
//
//    @Override
//    public boolean saveOrUpdate(MultipartFile entity) {
//        return false;
//    }
//
//    @Override
//    public MultipartFile getOne(Wrapper<MultipartFile> queryWrapper, boolean throwEx) {
//        return null;
//    }
//
//    @Override
//    public Map<String, Object> getMap(Wrapper<MultipartFile> queryWrapper) {
//        return Collections.emptyMap();
//    }
//
//    @Override
//    public <V> V getObj(Wrapper<MultipartFile> queryWrapper, Function<? super Object, V> mapper) {
//        return null;
//    }
//
//    @Override
//    public BaseMapper<MultipartFile> getBaseMapper() {
//        return null;
//    }
//
//    @Override
//    public Class<MultipartFile> getEntityClass() {
//        return null;
//    }
//}
