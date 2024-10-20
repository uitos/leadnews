package com.heima.wemedia.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.dtos.WmMaterialDto;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class WmMaterialServiceImpl  extends ServiceImpl<WmMaterialMapper , WmMaterial> implements WmMaterialService {
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.校验参数
        if (Objects.isNull(multipartFile)||multipartFile.getSize()==0){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2.处理业务
        //2.1上传素材到minIo
        try {
            String filename = multipartFile.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));
            filename = UUID.randomUUID().toString().replace("-", "").concat(suffixName);
//            String imageUrl = fileStorageService.uploadHtmlFile("", filename, multipartFile.getInputStream());
            String imageUrl = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
            //2.2保存素材到DB
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(wmUserId.intValue());
            wmMaterial.setUrl(imageUrl);
            wmMaterial.setType((short)0);
            wmMaterial.setIsCollection((short)0);
            wmMaterial.setCreatedTime(new Date());
            boolean flag = save(wmMaterial);
            if (!flag){
                //保存素材到数据库失败，为了保证数据一致性，上传MinIo成功的图片也应该删除
                fileStorageService.delete(imageUrl);
                throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
            }
            //3数据封装
            return ResponseResult.okResult(wmMaterial);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseResult pageQuery(WmMaterialDto dto) {
        //1.参数校验
        dto.checkParam();
        Long wmUserId = UserContext.getId();
        if (Objects.isNull(wmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2.处理业务
        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                .eq(WmMaterial::getUserId , wmUserId)
                .eq(!Objects.isNull(isCollection) && isCollection==1,WmMaterial::getIsCollection , isCollection)
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(page);
        //3.封装数据
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
