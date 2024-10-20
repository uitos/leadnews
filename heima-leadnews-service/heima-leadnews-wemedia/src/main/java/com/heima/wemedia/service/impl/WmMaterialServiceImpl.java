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

/**
 * @author mianbao
 * 1
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Resource
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult upload(MultipartFile multipartFile) throws IOException {

        Long userId = UserContext.getUserId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }

        if (Objects.isNull(multipartFile) || multipartFile.getSize() == 0) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        //上传
        String name = multipartFile.getName();
        //String affix = name.substring(name.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String path = fileStorageService.uploadImgFile("", fileName, multipartFile.getInputStream());

        //更新到DB
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(userId.intValue());
        wmMaterial.setUrl(path);
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());

        int result = wmMaterialMapper.insert(wmMaterial);
        if (result != 1) {
            //添加失败时删除
            fileStorageService.delete(path);
            throw new CustomException(AppHttpCodeEnum.SAVE_ERROR);
        }

        return ResponseResult.okResult(wmMaterial);
    }


    @Override
    public ResponseResult selectByPage(WmMaterialDto dto) {

        Long userId = UserContext.getUserId();
        if (Objects.isNull(userId)) {
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }

        if (Objects.isNull(dto.getPage()) || dto.getPage() < 0) {
            dto.setPage(1);
        }

        if (Objects.isNull(dto.getSize()) || dto.getSize() < 0 || dto.getSize() > 100) {
            dto.setSize(10);
        }

        Short isCollection = dto.getIsCollection();
        Page<WmMaterial> page = this.lambdaQuery()
                .eq(Objects.nonNull(isCollection) && isCollection == 1, WmMaterial::getIsCollection, isCollection)
                .eq(WmMaterial::getUserId, userId)
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(new Page<>(dto.getPage(), dto.getSize()));
        PageResponseResult pageResponseResult = new PageResponseResult();
        pageResponseResult.setCurrentPage((int) page.getCurrent());
        pageResponseResult.setTotal((int) page.getTotal());
        pageResponseResult.setSize((int) page.getSize());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

}
