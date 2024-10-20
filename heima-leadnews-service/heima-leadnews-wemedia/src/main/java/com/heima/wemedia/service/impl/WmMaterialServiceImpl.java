package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.utils.WmThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1. 校验参数
        if(multipartFile == null || multipartFile.getSize() == 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2. 处理业务

        //2.1 文件重命名
        String filename = multipartFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));//.jpg
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        filename = uuid.concat(suffix);

        //2.2 上传文件
        String path = null;
        try {
            path = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(AppHttpCodeEnum.FILE_UPLOAD_ERROR);
        }
        Integer wmUserId = WmThreadLocalUtil.getWmUserId();
        if(wmUserId == null) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //3. 封装参数
        WmMaterial material = new WmMaterial();
        material.setUserId(wmUserId);
        material.setUrl(path);
        material.setType((short) 0);
        material.setIsCollection((short) 0);
        material.setCreatedTime(new Date());
        save(material);
        return ResponseResult.okResult(material);
    }

    @Override
    public ResponseResult findPage(WmMaterialDto dto) {
        //1.检验参数
        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        Integer wmUserId = WmThreadLocalUtil.getWmUserId();
        if(Objects.isNull(wmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2. 处理业务
        // 分页
        IPage<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        // 组装条件
        Short isCollection = dto.getIsCollection();
        lambdaQuery()
                .eq(isCollection != null && isCollection ==1,WmMaterial::getIsCollection, isCollection)
                .eq(WmMaterial::getUserId, wmUserId)
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(page);
        //3. 封装参数
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int)page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
