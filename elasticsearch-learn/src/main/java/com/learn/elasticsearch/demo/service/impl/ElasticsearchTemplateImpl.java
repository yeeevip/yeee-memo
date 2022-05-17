package com.learn.elasticsearch.demo.service.impl;

import com.learn.elasticsearch.demo.mapping.BaseIndex;
import com.learn.elasticsearch.demo.service.ElasticsearchTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:59
 */
@Slf4j
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
        restTemplate.indexOps(clazz).delete();
    }

    @Override
    public BulkResponse bulk(List<? extends BaseIndex> list, String index, String type) throws IOException {
        List<IndexQuery> queries = list.stream().map(item -> new IndexQueryBuilder().withId(item.getId()).withObject(item).build()).collect(Collectors.toList());
        List<String> strings = restTemplate.bulkIndex(queries, IndexCoordinates.of(index));
        log.error("-----------------bulk res = {}----------------------", strings);
        return null;
    }

}
