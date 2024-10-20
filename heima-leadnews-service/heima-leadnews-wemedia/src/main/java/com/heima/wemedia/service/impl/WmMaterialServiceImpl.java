package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //参数校验
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //处理业务
        //1.上传素材到Minio
        try {
            String filename = multipartFile.getOriginalFilename();
            if (Objects.isNull(filename)) {
                throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
            }
            String suffixName = filename.substring(filename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "").concat(suffixName);
            String imageUrl = fileStorageService.uploadImgFile("", fileName, multipartFile.getInputStream());
            //2.保存素材到DB
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(wmUserId.intValue());
            wmMaterial.setUrl(imageUrl);
            wmMaterial.setType((short) 0);
            wmMaterial.setIsCollection((short) 0);
            wmMaterial.setCreatedTime(new Date());
            boolean flag = save(wmMaterial);
            if (!flag) {
                throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
            }
            //封装数据
            return ResponseResult.okResult(wmMaterial);

        } catch (IOException e) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    public ResponseResult pageQuery(WmMaterialDto dto) {
        dto.checkParam();
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        wmMaterialMapper.selectPage(page,
                new LambdaQueryWrapper<WmMaterial>()
                        .eq(WmMaterial::getUserId, wmUserId)
                        .eq(!Objects.isNull(isCollection) && isCollection == 1,WmMaterial::getIsCollection, isCollection));
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
