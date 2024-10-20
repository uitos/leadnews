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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    FileStorageService fileStorageService;
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        if(Objects.isNull(UserContext.getId())){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (Objects.isNull(multipartFile)||multipartFile.getSize()==0){
            throw new CustomException(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        try {
            String filename = multipartFile.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));
            filename = UUID.randomUUID().toString().replace("-", ",").concat(suffixName);
            String path = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());

            WmMaterial wmMaterial=new WmMaterial();
            wmMaterial.setUserId(UserContext.getId());
            wmMaterial.setUrl(path);
            wmMaterial.setType((short) 0);
            wmMaterial.setIsCollection((short) 0);
            wmMaterial.setCreatedTime(new Date());
            boolean save = save(wmMaterial);
            if (!save){
                fileStorageService.delete(path);
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            return ResponseResult.okResult(wmMaterial);
        } catch (IOException e) {
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }

    }

    @Override
    public ResponseResult queryByPage(WmMaterialDto dto) {
        dto.checkParam();
        if(Objects.isNull(UserContext.getId())){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page=new Page<>(dto.getPage(),dto.getSize());
        lambdaQuery()
                .eq(Objects.nonNull(isCollection)&& isCollection==1,WmMaterial::getIsCollection,dto.getIsCollection())
                .eq(WmMaterial::getUserId,UserContext.getId())
                .page(page);

        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;

    }
}
