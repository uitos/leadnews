package com.heima.wemedia.service.impl;

import com.aliyuncs.utils.StringUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Resource
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 校验参数
        if (Objects.isNull(UserContext.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if (Objects.isNull(multipartFile)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_ERROR);
        }
        // 处理业务
        // 获取原始文件名
        try {
            String fileName = multipartFile.getOriginalFilename();
            if(StringUtils.isEmpty(fileName)){
                throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
            }
            // 分割得到一个新文件名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID().toString().replace("-", "").concat(suffixName);
            String imageUrl = fileStorageService.uploadImgFile("", fileName, multipartFile.getInputStream());
            // 保存素材到表中
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(UserContext.getId().intValue());
            wmMaterial.setUrl(imageUrl);
            wmMaterial.setType((short)0);
            wmMaterial.setIsCollection((short)0);
            wmMaterial.setCreatedTime(new Date());
            boolean flag = save(wmMaterial);
            if(!flag){
                fileStorageService.delete(imageUrl);
                throw new CustomException(AppHttpCodeEnum.FILE_ERROR);
            }
        } catch (Exception e) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        return null;
    }

    @Override
    public ResponseResult pageList(WmMaterialDto dto) {
        // 1.校验参数
        dto.checkParam();
        Long wmUserId = UserContext.getId();
        if(Objects.isNull(wmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 二、处理业务
        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                .eq(WmMaterial::getUserId,wmUserId)
                .eq(!Objects.isNull(isCollection)&&isCollection==1,WmMaterial::getIsCollection,isCollection)
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(page);
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
