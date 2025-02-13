package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP已发布文章配置表 服务实现类
 * </p>
 *
 * @author ghy
 * @since 2023-09-21
 */
@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {

}
