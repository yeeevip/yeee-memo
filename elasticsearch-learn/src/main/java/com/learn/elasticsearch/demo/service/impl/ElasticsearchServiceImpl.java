package com.learn.elasticsearch.demo.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.learn.elasticsearch.demo.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private final RestHighLevelClient restClient;

    @Override
    public boolean createIndex(String indexName, String typeName, String mapping) throws IOException {

        // 创建索引
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        restClient.indices().create(indexRequest, RequestOptions.DEFAULT);

        // 构建一个Index（索引）
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName).source(mapping, XContentType.JSON);

        // 同步执行
        AcknowledgedResponse acknowledgedResponse = restClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
        boolean acknowledged = acknowledgedResponse.isAcknowledged();
        if (!acknowledged) {
            log.info("index create error");
            return false;
        }
        return true;
    }

    @Override
    public boolean exists(String indexName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    @Override
    public long count(QueryBuilder queryBuilder, String... indices) throws Exception {
        CountRequest countRequest = new CountRequest(indices);
        countRequest.query(queryBuilder);
        CountResponse countResponse = restClient.count(countRequest, RequestOptions.DEFAULT);
        return countResponse.getCount();
    }

    @Override
    public void delete(String indexName) throws Exception {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        restClient.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Override
    public BulkResponse bulk(List<Map<String, Object>> list, String index, String type) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            // 字段类型处理
            //convertFieldType(map, index);
            // TODO 简单处理日期字段
            String jsonStr = JSONUtil.toJsonStr(map);
            bulkRequest.add(new IndexRequest(index, type, map.get("id").toString()).source(JSONObject.parseObject(jsonStr)));
        }
        return restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
}
