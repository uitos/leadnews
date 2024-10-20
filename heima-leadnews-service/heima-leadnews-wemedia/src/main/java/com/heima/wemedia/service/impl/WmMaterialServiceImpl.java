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
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @ClassName WmMaterialServiceImpl
 * @Description TODO(描述该类的功能)
 * @Author Zhu Rui
 * @Date 2024/10/20
 * @Version 1.0
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.校验参数
        if (Objects.isNull(multipartFile) || multipartFile.getSize() == 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2.处理业务
        //2.1上传素材到MinIO
        try {
            String fileName = multipartFile.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
            }
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID().toString().replace("_", "").concat(suffixName);
            String imageUrl = fileStorageService.uploadImgFile("", fileName, multipartFile.getInputStream());
            //2.2保存素材到DB
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(wmUserId.intValue());
            wmMaterial.setUrl(imageUrl);
            wmMaterial.setType((short) 0);
            wmMaterial.setIsCollection((short) 0);
            wmMaterial.setCreatedTime(new Date());
            boolean flag = save(wmMaterial);
            if (!flag) {
                fileStorageService.delete(imageUrl);
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
            }
            //3.封装数据
            return ResponseResult.okResult(wmMaterial);
        } catch (IOException e) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }

    }

    @Override
    public ResponseResult queryPage(WmMaterialDto dto) {
        //1.参数校验
        dto.checkParam();
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2.处理业务
        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                .eq(WmMaterial::getUserId, wmUserId)
                .orderByDesc(WmMaterial::getCreatedTime)
                .eq(!Objects.isNull(isCollection) && isCollection == 1, WmMaterial::getIsCollection, isCollection)
                .page(page);
        //3.封装数据
        PageResponseResult result = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
