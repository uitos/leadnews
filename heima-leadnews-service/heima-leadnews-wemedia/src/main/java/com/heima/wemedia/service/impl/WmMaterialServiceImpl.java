package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 15:41:39
 */
@Service
@Slf4j
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 一.校验参数
        if(Objects.isNull(multipartFile) || multipartFile.getSize() == 0){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long wmUserId = UserContext.getId();
        if(Objects.isNull(wmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 二.处理业务
        // 1.上传素材到MinIO
        try {
            String filename = multipartFile.getOriginalFilename();
            if(StringUtils.isBlank(filename)){
                throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
            }
            String suffixName = filename.substring(filename.lastIndexOf("."));
            filename = UUID.randomUUID().toString().replace("-", "").concat(suffixName);
            String imageUrl = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
            // 2.保存素材到DB
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(wmUserId.intValue());
            wmMaterial.setUrl(imageUrl);
            wmMaterial.setType((short)0);
            wmMaterial.setIsCollection((short)0);
            wmMaterial.setCreatedTime(new Date());
            boolean flag = save(wmMaterial);
            if(!flag) {
                //保存素材到数据库失败，为了保证数据一致性，上传MinIO成功的图片，也应该删除
                fileStorageService.delete(imageUrl);
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            // 三.封装数据
            return ResponseResult.okResult(wmMaterial);
        } catch (IOException e) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    public ResponseResult pageQuery(WmMaterialDto dto) {
        // 一.校验参数
        dto.checkParam();
        Long wmUserId = UserContext.getId();
        if(Objects.isNull(wmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 二.处理业务
        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                .eq(WmMaterial::getUserId, wmUserId)
                .eq(!Objects.isNull(isCollection) && isCollection == 1,
                        WmMaterial::getIsCollection, isCollection)
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(page);
        // 三.封装数据
        PageResponseResult pageResponseResult = new PageResponseResult(
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
