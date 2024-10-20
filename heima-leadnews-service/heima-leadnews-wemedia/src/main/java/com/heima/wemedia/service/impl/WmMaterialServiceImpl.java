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
    public ResponseResult queryPage(WmMaterialDto dto) {
        // 检查传入的DTO是否为null，如果为null则抛出参数无效异常
        if (Objects.isNull(dto)) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 调用DTO的参数检查方法，进一步验证参数的有效性
        dto.checkParam();
        // 获取当前用户的ID，用于后续查询条件
        Long userId = UserContext.getId();
        // 检查用户ID是否为null，如果为null则抛出需要登录异常
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 获取是否收藏的标志，用于后续查询条件
        Short isCollection = dto.getIsCollection();
        // 创建分页对象，用于存储查询结果
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        this.lambdaQuery()
                // 查询当前用户的图片
                .eq(WmMaterial::getUserId, userId)
                // 判断是否收藏，如果为1则查询收藏的图片，否则查询全部图片
                .eq(!Objects.isNull(isCollection) && isCollection == 1, WmMaterial::getIsCollection, isCollection)
                // 按创建时间降序排序
                .orderByDesc(WmMaterial::getCreatedTime)
                // 执行分页查询
                .page(page);
        // 创建分页响应结果对象，用于存储分页信息和查询结果
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        // 设置查询结果到分页响应结果对象中
        pageResponseResult.setData(page.getRecords());
        // 返回分页响应结果
        return pageResponseResult;
    }

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile) || multipartFile.getSize() == 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_IMAGE_FORMAT_ERROR);
        }
        Long userId = UserContext.getId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        String fileName = multipartFile.getOriginalFilename();
        String substring = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString().replace("-", "").concat(substring);
        String imageUrl = null;
        try {
            imageUrl = fileStorageService.uploadImgFile("leadnews", fileName, multipartFile.getInputStream());
        } catch (IOException e) {
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(userId.intValue());
        wmMaterial.setUrl(imageUrl);
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());
        boolean save = save(wmMaterial);
        if (!save) {
            fileStorageService.delete(imageUrl);
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
        return ResponseResult.okResult(wmMaterial);
    }
}
