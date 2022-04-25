package com.learn.elasticsearch.demo.service.impl;

import com.learn.elasticsearch.demo.service.ElasticsearchTemplate;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:59
 */
@RequiredArgsConstructor
@Service
public class ElasticsearchTemplateImpl implements ElasticsearchTemplate {

    private final ElasticsearchRestTemplate restTemplate;

    @Override
    public boolean createIndex(Class<?> clazz) throws IOException {
        return restTemplate.indexOps(clazz).create();
    }

    @Override
    public boolean exists(Class<?> clazz) throws Exception {
        return restTemplate.indexOps(clazz).exists();
    }

    @Override
    public long count(QueryBuilder queryBuilder, String... indices) throws Exception {
        return 0;
    }

    @Override
    public void delete(Class<?> clazz) throws Exception {

    }

    @Override
    public BulkResponse bulk(List<Map<String, Object>> list, String index, String type) throws IOException {
        return null;
    }
}
