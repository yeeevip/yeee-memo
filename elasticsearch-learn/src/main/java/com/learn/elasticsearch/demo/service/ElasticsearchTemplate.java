package com.learn.elasticsearch.demo.service;

import com.learn.elasticsearch.demo.mapping.BaseIndex;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:58
 */
public interface ElasticsearchTemplate {

    /**
     * 创建索引
     */
    boolean createIndex(Class<?> clazz) throws IOException;

    /**
     * 判断索引是否存在
     */
    boolean exists(Class<?> clazz) throws Exception;

    /**
     * 统计数量
     */
    long count(QueryBuilder queryBuilder, String... indices) throws Exception;

    /**
     * 删除索引
     */
    void delete(Class<?> clazz) throws Exception;

    /**
     * 批量保存
     */
    List<String> bulk(List<? extends BaseIndex> list, String indexName) throws IOException;

}
