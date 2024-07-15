package com.heima.common.constants;


/**
 * APP端搜索的常量
 */
public interface ArticleConstants {

    /**
     * 加载更多
     */
    Short LOADTYPE_LOAD_MORE = 1;

    /**
     * 加载最新
     */
    Short LOADTYPE_LOAD_NEW = 2;

    /**
     * 频道
     */
    String DEFAULT_TAG = "__all__";

    String ARTICLE_ES_SYNC_TOPIC = "article.es.sync.topic";

    Integer HOT_ARTICLE_LIKE_WEIGHT = 3;
    Integer HOT_ARTICLE_COMMENT_WEIGHT = 5;
    Integer HOT_ARTICLE_COLLECTION_WEIGHT = 8;
    String HOT_ARTICLE_FIRST_PAGE = "hot_article_first_page_";

}