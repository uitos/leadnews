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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Resource
    private FileStorageService fileStorageService;
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.校验文件
        if (Objects.isNull(multipartFile)||multipartFile.getSize()==0){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long vmUserId = UserContext.getId();
        if (Objects.isNull(vmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2.处理业务
        //2.1 上传素材到minio
        try {
            String filename = multipartFile.getOriginalFilename();
            if (StringUtils.isBlank(filename)){
                throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
            }
            String suffixName = filename.substring(filename.lastIndexOf("."));
            filename = UUID.randomUUID().toString().replace("-", "").concat(suffixName);
            String imageUrl = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
            //2.2 保存素材信息到DB
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setUserId(vmUserId.intValue());
            wmMaterial.setUrl(imageUrl);
            wmMaterial.setType((short) 0);
            wmMaterial.setIsCollection((short) 0);
            wmMaterial.setCreatedTime(new Date());
            boolean flag = save(wmMaterial);
            //保存素材到数据库失败，为了保证数据一致性，删除上传minIO成功的图片
            if (!flag){
                fileStorageService.delete(imageUrl);
                throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
            }
            //封装数据
            return ResponseResult.okResult(wmMaterial);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseResult pageQuery(WmMaterialDto dto) {
        //校验参数
        dto.checkParam();
        Long vmUserId = UserContext.getId();
        if (Objects.isNull(vmUserId)){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //处理业务
        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery().eq(WmMaterial::getUserId,vmUserId)
                .eq(!Objects.isNull(isCollection)&&isCollection==1,WmMaterial::getIsCollection,isCollection)
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(page);
        //封装数据
        PageResponseResult pageResponseResult = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;


    }
}
