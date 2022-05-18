package com.learn.elasticsearch.demo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.learn.ElasticsearchApplication;
import com.learn.db.opr.ITProjectService;
import com.learn.db.opr.TProject;
import com.learn.elasticsearch.demo.service.ElasticsearchService;
import com.learn.model.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:41
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchServiceTest {

    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private ITProjectService iTProjectService;

    @Test
    public void testCreateIndex() throws Exception {
        String indexName = "cf_project";
        String mappingSource = "{\n" +
                "\t\"properties\": {\n" +
                "\t\t\"id\": {\n" +
                "\t\t\t\"type\": \"long\"\n" +
                "\t\t},\n" +
                "\t\t\"categoryId\": {\n" +
                "\t\t\t\"type\": \"keyword\"\n" +
                "\t\t},\n" +
                "\t\t\"title\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"standard\",\n" +
                "\t\t\t\"search_analyzer\": \"standard\",\n" +
                "\t\t\t\"fields\": {\n" +
                "\t\t\t\t\"keyword\": {\n" +
                "\t\t\t\t\t\"type\": \"keyword\",\n" +
                "\t\t\t\t\t\"ignore_above\": 256\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"blurb\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"standard\",\n" +
                "\t\t\t\"search_analyzer\": \"standard\",\n" +
                "\t\t\t\"fields\": {\n" +
                "\t\t\t\t\"keyword\": {\n" +
                "\t\t\t\t\t\"type\": \"keyword\",\n" +
                "\t\t\t\t\t\"ignore_above\": 256\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"createTime\": {\n" +
                "\t\t\t\"type\": \"date\",\n" +
                "\t\t\t\"fields\": {\n" +
                "\t\t\t\t\"keyword\": {\n" +
                "\t\t\t\t\t\"type\": \"keyword\",\n" +
                "\t\t\t\t\t\"ignore_above\": 256\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        log.info("-------------------index = {}，mapping = {}-------------------", indexName, mappingSource);
        boolean exists = elasticsearchService.exists(indexName);
        log.info("-----------------exists = {}---------------------", exists);
        if (!exists) {
            boolean create = elasticsearchService.createIndex(indexName, mappingSource);
            log.info("-----------------create = {}---------------------", create);
        }
    }

    @Test
    public void testBatchSyncData() throws Exception {
        List<TProject> list = iTProjectService.list();
        List<Map<String, Object>> mapList = list.stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", item.getId());
                    map.put("categoryId", item.getCategoryId());
                    map.put("title", item.getTitle());
                    map.put("blurb", item.getBlurb());
                    map.put("createTime", item.getLaunchDateRaising());
                    return map;
                })
                .collect(Collectors.toList());
        BulkResponse response = elasticsearchService.bulk("cf_project", mapList);
        log.info("-----------------response.hasFailures = {}---------------------", response.buildFailureMessage());
    }

    @Test
    public void testConditionPageSearch() throws IOException {
        Integer pageNum = 1, pageSize = 20;
        String keyword = "北京";
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("id").gt(1));
        queryBuilder.must(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhraseQuery("title", keyword))
                .should(QueryBuilders.matchPhraseQuery("blurb", keyword)));
        PageVO<SearchHit> pageVO = elasticsearchService.pageSearch(pageNum, pageSize, queryBuilder, "cf_project");
        log.info("---------pageVO = {}-----------", pageVO);
    }

    @Test
    public void testAggregationSearch() throws IOException {
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("category").field("categoryId")
                .subAggregation(AggregationBuilders.dateHistogram("createDate").field("createTime").calendarInterval(DateHistogramInterval.MONTH).minDocCount(1))
                .size(500);
        Aggregations aggregations = elasticsearchService.aggregationSearch(aggregationBuilder, "cf_project");
        log.info("---------aggregations = {}-----------", ((ParsedLongTerms)aggregations.asMap().get("category")).getBuckets().get(0).getDocCount());
    }

    @Test
    public void testCount() throws Exception {
        long count = elasticsearchService.count(QueryBuilders.matchAllQuery(), "cf_project");
        log.info("-----------------count = {}---------------------", count);
    }

    @Test
    public void testDelIndex() throws Exception {
        elasticsearchService.delete("cf_project");
        log.info("-----------------del---------------------");
    }

}
