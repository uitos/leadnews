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
import java.util.UUID;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 14:59:55
 */
@Service
@Slf4j
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 一.校验参数
        if(multipartFile == null || multipartFile.getSize() == 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 二.处理业务
        // 1.文件重命名
        String filename = multipartFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));//.jpg
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        filename = uuid.concat(suffix);
        // 2.上传
        String path = null;
        try {
            path = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(AppHttpCodeEnum.FILE_UPLOAD_ERROR);
        }
        // 三.封装数据
        Integer wmUserId = WmThreadLocalUtil.getWmUserId();
        if(wmUserId == null) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(wmUserId);
        wmMaterial.setUrl(path);
        wmMaterial.setType((short)0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findPage(WmMaterialDto dto) {
        // 一.校验参数
        if(dto == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页参数校验
        dto.checkParam();
        Integer wmUserId = WmThreadLocalUtil.getWmUserId();
        if(wmUserId == null) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 二.处理业务
        // 分页
        IPage<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        // 组装条件
        Short isCollection = dto.getIsCollection();
        this.lambdaQuery()
                .eq(isCollection != null && isCollection == 1, WmMaterial::getIsCollection, isCollection)
                .eq(WmMaterial::getUserId, wmUserId)
                .orderByDesc(WmMaterial::getCreatedTime)  // 排序
                .page(page);
        // 三.封装数据
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
