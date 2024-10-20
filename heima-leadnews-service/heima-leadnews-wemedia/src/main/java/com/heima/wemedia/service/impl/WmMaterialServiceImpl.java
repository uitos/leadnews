package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wm.dtos.WmMaterialDto;
import com.heima.model.wm.pojos.WmMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.utils.WmUserContext;
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
    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 自媒体图片上传
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        if(Objects.isNull(multipartFile)&&multipartFile.getSize() == 0){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        Long user = WmUserContext.getUserId();
        if(user == null){
            throw new CustomException(AppHttpCodeEnum.NEED_LOGIN);
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //获取文件名
        String filename = multipartFile.getOriginalFilename();
        //截取文件名后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = uuid+".jpg";
        String url = "";
        try {
            url = fileStorageService.uploadImgFile("",filename,multipartFile.getInputStream());
        } catch (IOException e) {
            throw new CustomException(AppHttpCodeEnum.PARAM_IMAGE_ERROR);
        }
        //封装返回数据
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(user);
        wmMaterial.setUrl(url);
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());
        int insert = wmMaterialMapper.insert(wmMaterial);
        log.info("上传图片{}",insert>=1?"成功":"失败" );
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findByPage(WmMaterialDto dto) {

        if(Objects.isNull(dto)){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        //校验分页参数是否有效
        dto.checkParam();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        QueryWrapper<WmMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmMaterial::getIsCollection,dto.getIsCollection());
        wmMaterialMapper.selectPage
                (page, queryWrapper);
        PageResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}