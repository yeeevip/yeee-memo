package vip.yeee.memo.demo.elasticsearch;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import vip.yeee.memo.demo.elasticsearch.domain.es.entity.BaseIndex;
import vip.yeee.memo.demo.elasticsearch.domain.es.entity.TProjectIndex;
import vip.yeee.memo.demo.elasticsearch.domain.es.repository.EsRestTemplateRepository;
import vip.yeee.memo.demo.elasticsearch.domain.mysql.entity.TProject;
import vip.yeee.memo.demo.elasticsearch.service.ITProjectService;
import vip.yeee.memo.demo.elasticsearch.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:41
 */
@Slf4j
@SpringBootTest(classes = ElasticsearchApplication.class)
public class EsRestTemplateRepositoryTests {

    @Autowired
    private EsRestTemplateRepository esRestTemplateRepository;
    @Autowired
    private ITProjectService iTProjectService;

    @Test
    public void testCreateIndex() throws Exception {
        boolean exists = esRestTemplateRepository.exists(TProjectIndex.class);
        log.info("-----------------exists = {}---------------------", exists);
        if (!exists) {
            boolean create = esRestTemplateRepository.createIndex(TProjectIndex.class);
            log.info("-----------------create = {}---------------------", create);
        }
    }

    @Test
    public void testBulk() throws IOException {
        List<TProject> list = iTProjectService.list();
        List<TProjectIndex> myIndexList = list.stream()
                .map(item -> {
                    TProjectIndex projectIndex = new TProjectIndex();
                    BeanUtils.copyProperties(item, projectIndex);
                    projectIndex.setId(item.getId());
                    projectIndex.setCreateTime(item.getLaunchDateRaising());
                    return projectIndex;
                })
                .collect(Collectors.toList());
        List<IndexedObjectInformation> res = esRestTemplateRepository.bulk(myIndexList, "cf_project_2");
        log.info("-------------bulk res = {}------------------", res);
    }

    @Test
    public void testSaveBatch() throws IOException {
        List<TProject> list = iTProjectService.list();
        List<TProjectIndex> myIndexList = list.stream()
                .map(item -> {
                    TProjectIndex projectIndex = new TProjectIndex();
                    BeanUtils.copyProperties(item, projectIndex);
                    projectIndex.setId(item.getId());
                    projectIndex.setCreateTime(item.getLaunchDateRaising());
                    return projectIndex;
                })
                .collect(Collectors.toList());
        Iterable<? extends BaseIndex> res = esRestTemplateRepository.saveBatch(myIndexList);
        log.info("-------------bulk res = {}------------------", res);
    }

    /**
     * //进行单个值得精确匹配termQuery("key", obj) 完全匹配
     * QueryBuilders.termQuery("name","我是中国人")
     *
     * //进行多个值得精确匹配termsQuery("key", obj1, obj2..)   一次匹配多个值
     * 用于keyword类型
     * String[] a = {"a","ab","ac","qq"}
     * QueryBuilders.termsQuery("name",a)
     *
     *
     * //进行模糊查询multiMatchQuery("key.keyword", "*field1*"); 可以使用通配符
     * ！！！ 查询的字段类型为keyword，如果text的话匹配不准确
     * QueryBuilders.wildcardQuery("name"/"name.keyword","*我是中*")
     *
     *
     * //对关键字分词后全文检索，eg：找到所有‘中国’、‘加油’的索引后回查doc返回
     * text
     * QueryBuilders.matchQuery("name", "中国 加油")
     *
     * //对关键字分词后全文检索，再次过滤doc匹配，eg：找到所有‘中国’、‘加油’的索引后回查doc后再次过滤
     * text
     * queryBuilders.matchPhraseQuery("key", "中国 加油")
     *
     *
     * //进行布尔查询
     * QueryBuilders.boolQuery()
     *
     * //进行模糊查询
     * QueryBuilders.boolQuery()
     *
     * //进行范围查询 gte大于等于,lte小于等于 gt大于 lt小于
     * QueryBuilders.rangeQuery("age")
     *     .gte(jsonObjectEq.get("1"))
     *     .lte(jsonObjectEq.get("10")))
     * ......
     */
    @Test
    public void testConditionPageSearch() {
        String keyword = "北京";
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("id").gt(1));
        queryBuilder.must(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhraseQuery("title", keyword))
                .should(QueryBuilders.matchPhraseQuery("content", keyword)));
        PageVO<TProjectIndex> pageVO = esRestTemplateRepository.pageSearch(1, 20, queryBuilder, null, TProjectIndex.class);
        log.info("---------pageVO = {}-----------", pageVO);
    }

    @Test
    public void testAggregationSearch() {
        // 按categoryId 分组group by，并在各分组内按月统计
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("category").field("categoryId")
                .subAggregation(AggregationBuilders.dateHistogram("createDate").field("createTime").calendarInterval(DateHistogramInterval.MONTH).minDocCount(1))
                .size(500);
        Aggregations aggregations = esRestTemplateRepository.aggregationSearch(aggregationBuilder, TProjectIndex.class);

        List<? extends Terms.Bucket> buckets = ((ParsedStringTerms)aggregations.get("category")).getBuckets();
        for (Terms.Bucket bucket : buckets) {
            // 每组的key
            String key = (String) bucket.getKey();
            // long docCount = bucket.getDocCount();
        }
        log.info("---------aggregations = {}-----------", buckets.get(0).getDocCount());
    }

    @Test
    public void testExists() throws Exception {
        boolean exists = esRestTemplateRepository.exists(TProjectIndex.class);
        log.info("---------------exists = {}----------------", exists);
    }

    @Test
    public void testDelIndex() throws Exception {
        esRestTemplateRepository.delete(TProjectIndex.class);
        log.info("---------------del----------------");
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
        PageVO<TProjectIndex> pageVO = esRestTemplateRepository.pageSearch(0, 50, queryBuilder, sortBuilder, TProjectIndex.class);
        log.info("-----------------geoDistancePageVO = {} ---------------------", pageVO);
    }

}
