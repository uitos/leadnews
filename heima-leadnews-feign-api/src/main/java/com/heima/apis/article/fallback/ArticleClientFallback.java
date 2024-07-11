package com.heima.apis.article.fallback;

import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-09 15:58:37
 */
@Component
@Slf4j
public class ArticleClientFallback implements IArticleClient {

    /**
     * 保存文章数据 降级处理
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveApArticle(ArticleDto dto) {
        log.warn("保存文章失败了，进入了降级逻辑");
        return ResponseResult.okResult(-1L);
    }
}
