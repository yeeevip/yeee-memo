package vip.yeee.memo.demo.elasticsearch;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import vip.yeee.memo.demo.elasticsearch.domain.es.repository.EsRestClientRepository;
import vip.yeee.memo.demo.elasticsearch.domain.mysql.entity.TProject;
import vip.yeee.memo.demo.elasticsearch.service.ITProjectService;
import vip.yeee.memo.demo.elasticsearch.vo.PageVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
@SpringBootTest(classes = ElasticsearchApplication.class)
public class EsRestClientRepositoryTest {

    @Autowired
    private EsRestClientRepository esRestClientRepository;
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
        boolean exists = esRestClientRepository.exists(indexName);
        log.info("-----------------exists = {}---------------------", exists);
        if (!exists) {
            boolean create = esRestClientRepository.createIndex(indexName, mappingSource);
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
        BulkResponse response = esRestClientRepository.bulk("cf_project", mapList);
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
        PageVO<SearchHit> pageVO = esRestClientRepository.pageSearch(pageNum, pageSize, queryBuilder, null, "cf_project");
        log.info("---------pageVO = {}-----------", pageVO);
    }

    @Test
    public void testAggregationSearch() throws IOException {
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("category").field("categoryId")
                .subAggregation(AggregationBuilders.dateHistogram("createDate").field("createTime").calendarInterval(DateHistogramInterval.MONTH).minDocCount(1))
                .size(500);
        Aggregations aggregations = esRestClientRepository.aggregationSearch(aggregationBuilder, "cf_project");
        log.info("---------aggregations = {}-----------", ((ParsedLongTerms)aggregations.asMap().get("category")).getBuckets().get(0).getDocCount());
    }

    @Test
    public void testCount() throws Exception {
        long count = esRestClientRepository.count(QueryBuilders.matchAllQuery(), "cf_project");
        log.info("-----------------count = {}---------------------", count);
    }

    @Test
    public void testDelIndex() throws Exception {
        esRestClientRepository.delete("cf_project");
        log.info("-----------------del---------------------");
    }

    /**
     * ## 创建索引
     * PUT http://localhost:9200/location_test/_mapping
     * {
     *     "properties":{
     *         "locationName":{
     *             "type":"text",
     *             "analyzer":"ik_max_word"
     *         },
     *         "location":{
     *             "type":"geo_point"
     *         }
     *     }
     * }
     *
     * ## 插入数据
     * POST http://localhost:9200/location_test/_doc
     * {
     * 	"locationName": "召稼楼古镇",
     * 	"location": {
     * 		"lat": 31.081128,
     * 		"lon": 121.555511
     *        }
     * }
     * {
     * 	"locationName": "上海奇思妙想减压馆",
     * 	"location": {
     * 		"lat": 31.24249,
     * 		"lon": 121.490283
     *    }
     * }
     */
    @Test
    public void testGeoDistanceQuery() throws Exception {

        double curLat = 39.929986;
        double curLon = 116.395645;

        // 查询某经纬度100米范围内
        GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(curLat, curLon)
                .distance(100, DistanceUnit.METERS);
        GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort("location", curLat, curLon)
                .unit(DistanceUnit.METERS)
                .order(SortOrder.ASC);
        PageVO<SearchHit> pageVO = esRestClientRepository.pageSearch(0, 50, queryBuilder, sortBuilder, "location_test");
        log.info("-----------------geoDistancePageVO = {} ---------------------", pageVO);
    }

}
