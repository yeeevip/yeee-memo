package vip.yeee.memo.integrate.elasticsearch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import com.google.common.collect.Maps;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.*;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import vip.yeee.memo.integrate.elasticsearch.opr.ITProjectService;
import vip.yeee.memo.integrate.elasticsearch.opr.TProject;
import vip.yeee.memo.integrate.elasticsearch.mapping.TProjectIndex;
import vip.yeee.memo.integrate.elasticsearch.repository.TProjectIndexRepository;
import vip.yeee.memo.integrate.elasticsearch.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
public class ElasticsearchRepositoryTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    // 直接操作文档
    @Autowired
    private TProjectIndexRepository projectIndexRepository;
    @Autowired
    private ITProjectService iTProjectService;

    @Test
    public void createIndex() {
        log.info("系统初始化会自动扫描实体类中的@Document注解创建索引");
    }

    /**
     * 判断索引存在
     */
    @Test
    public void testExistsIndex() {
        boolean flag = elasticsearchRestTemplate.indexOps(TProjectIndex.class).exists();
        log.info("flag = {}", flag);
    }

    /**
     * 删除索引
     */
    @Test
    public void testDeleteIndex() {
        boolean flag = elasticsearchRestTemplate.indexOps(TProjectIndex.class).delete();
        log.info("flag = {}", flag);
    }

    /**
     * 删除索引数据
     */
    @Test
    public void testDeleteData() {
        // 数据库中的数据
        List<TProject> dbDataList = iTProjectService.list();
        List<Integer> idList = dbDataList
                .stream()
                .map(TProject::getId)
                .collect(Collectors.toList());
        projectIndexRepository.deleteAllById(idList);
    }

    /**
     * 批量同步到ES
     */
    @Test
    public void testBatchSaveData() {
        // 数据库中的数据
        List<TProject> dbDataList = iTProjectService.list();
        // 构建索引数据
        List<TProjectIndex> esDataList = dbDataList.stream()
                .map(item -> {
                    TProjectIndex projectIndex = new TProjectIndex();
                    BeanUtils.copyProperties(item, projectIndex);
                    projectIndex.setId(item.getId());
                    projectIndex.setCreateTime(item.getLaunchDateRaising());
                    return projectIndex;
                })
                .collect(Collectors.toList());
        // 同步到es
        Iterable<TProjectIndex> res = projectIndexRepository.saveAll(esDataList);
        log.info("-------------bulk res = {}------------------", res);
    }

    /**
     * 查找全部
     */
    @Test
    public void testFindAllData() {
        Page<TProjectIndex> res = (Page<TProjectIndex>) projectIndexRepository.findAll();
        log.info("-------------bulk res = {}------------------", res.getTotalElements());
    }

    /**
     * 根据方法名称自动识别查询条件
     */
    @Test
    public void testQueryCreationMechanism() {
        List<TProjectIndex> list1 = projectIndexRepository.queryByTitleOrBlurbOrderByIdDesc("书", "一");
        log.info("----------------------------- list1 = {} --------------------------------------", list1);
        List<TProjectIndex> list2 = projectIndexRepository.findByTitleOrBlurbOrderByIdDesc("书", "一");
        log.info("----------------------------- list2 = {} --------------------------------------", list2);
        Page<TProjectIndex> page = projectIndexRepository
                .findByTitleOrBlurbOrderByIdDesc("书", "一", PageRequest.of(0, 5));
        log.info("----------------------------- page = {} --------------------------------------", page);
    }

    /**
     * 使用ElasticsearchRestTemplate条件构造分页查询
     */
    @Test
    public void testUseRestTemplatePageSearch() {
        Integer pageNum = 1, pageSize = 5;
        String keyword = "北京我爱你，一年之计在于晨";
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /* --------------------------------------精确查询-------------------------------------- */
//        /* --------------------------------------1-------------------------------------- */
//        // queryStringQuery 指定字符串作为关键词查询，关键词支持分词
//        boolQueryBuilder.should(QueryBuilders.queryStringQuery(keyword).field("title").field("content"));
//        /* --------------------------------------2-------------------------------------- */
//        // termQuery termsQuery 指定字符串作为关键词查询，关键词不支持分词
//        boolQueryBuilder.should(QueryBuilders.termQuery("title", keyword));
//        boolQueryBuilder.should(QueryBuilders.termQuery("content", keyword));
//        /* --------------------------------------3-------------------------------------- */
//        // matchQuery multiMatchQuery 指定字符串作为关键词查询，关键词支持分词
//        boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword));
//        boolQueryBuilder.should(QueryBuilders.matchQuery("content", keyword));
//        /* --------------------------------------模糊查询-------------------------------------- */
//        /* --------------------------------------1-------------------------------------- */
//        // 左右模糊查询，其中fuzziness的参数作用是在查询时，es动态的将查询关键词前后增加或者删除一个词，然后进行匹配
//        boolQueryBuilder.should(QueryBuilders.fuzzyQuery("title", keyword).fuzziness(Fuzziness.ONE));
//        boolQueryBuilder.should(QueryBuilders.fuzzyQuery("content", keyword).fuzziness(Fuzziness.ONE));
//        /* --------------------------------------2-------------------------------------- */
//        // 前缀查询
//        boolQueryBuilder.should(QueryBuilders.prefixQuery("title", keyword));
//        boolQueryBuilder.should(QueryBuilders.prefixQuery("content", keyword));
//        /* --------------------------------------3-------------------------------------- */
//        // 通配符查询，支持*和？，？表示单个字符；注意不建议将通配符作为前缀，否则导致查询很慢
//        boolQueryBuilder.should(QueryBuilders.wildcardQuery("title", "*" + keyword + "*"));
//        boolQueryBuilder.should(QueryBuilders.wildcardQuery("content", "*" + keyword + "*"));
//        /* --------------------------------------范围查询-------------------------------------- */
//        /* --------------------------------------1-------------------------------------- */
//        // 闭区间查询
//        boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").from("2020-01-01 18:01:01").to("2022-01-01 18:01:01"));
//        /* --------------------------------------2-------------------------------------- */
//        // 开区间查询，默认是true，也就是包含
//        boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").from("2022-01-01 18:01:01").to("2022-01-01 18:01:01").includeUpper(false).includeLower(false));
//        /* --------------------------------------3-------------------------------------- */
//        boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").gt("2022-01-01 18:01:01"));


        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
        SearchHits<TProjectIndex> hits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), TProjectIndex.class);
        PageVO<TProjectIndex> page2 = new PageVO<>(pageNum, pageSize);
        page2.setTotal(hits.getTotalHits());
        page2.setResult(hits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()));
        log.info("----------------------------- page2 = {} --------------------------------------", page2);
    }

    /**
     * 【聚合查询】
     *
     * 【Terms Aggregation】：根据词条内容分组，词条内容完全匹配的为一组
     *
     */
    @Test
    public void testTermsAggregationSearch() {

        /* --------------------------------------词条桶：Terms Aggregation-------------------------------------- */
        // eg.根据类型分组，并统计每组的个数及某一属性的最大、最小、平均值...
        NativeSearchQueryBuilder termsNativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 分桶
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("type_group").field("categoryId");
        // 桶内统计
        termsAggregationBuilder.subAggregation(AggregationBuilders.count("count_val").field("id"));
        termsAggregationBuilder.subAggregation(AggregationBuilders.avg("avg_val").field("id"));
        termsAggregationBuilder.subAggregation(AggregationBuilders.min("min_val").field("id"));
        termsAggregationBuilder.subAggregation(AggregationBuilders.max("max_val").field("id"));
        termsAggregationBuilder.subAggregation(AggregationBuilders.sum("sum_val").field("id"));
        // 执行查询
        termsNativeSearchQueryBuilder.withAggregations(termsAggregationBuilder);
        SearchHits<TProjectIndex> searchHits = elasticsearchRestTemplate.search(termsNativeSearchQueryBuilder.build(), TProjectIndex.class);

        if (searchHits.getAggregations() == null) {
            return;
        }
        // 处理结果
        Aggregations aggregations = (Aggregations) searchHits.getAggregations().aggregations();
        List<Map<String, Object>> termsGroupData = ((Terms)aggregations.get("type_group"))
                .getBuckets()
                .stream()
                .map(bucket -> {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("key", bucket.getKey());
                    map.put("count", bucket.getDocCount());
                    if (CollectionUtil.isNotEmpty(bucket.getAggregations())) {
                        Aggregations subAgg = bucket.getAggregations();
                        map.put("count_val", ((ParsedValueCount)subAgg.get("count_val")).getValue());
                        map.put("avg_val", ((ParsedAvg)subAgg.get("avg_val")).getValue());
                        map.put("min_val", ((ParsedMin)subAgg.get("min_val")).getValue());
                        map.put("max_val", ((ParsedMax)subAgg.get("max_val")).getValue());
                        map.put("sum_val", ((ParsedSum)subAgg.get("sum_val")).getValue());
                    }
                    return map;
                })
                .collect(Collectors.toList());
        log.info("termsGroupData = {}", termsGroupData);
    }

    /**
     * 【聚合查询】
     *
     * 【Histogram Aggregation】：根据数值阶梯分组，与日期类似
     *
     */
    @Test
    public void testHistogramAggregationSearch() {

        /* --------------------------------------阶梯桶：Histogram Aggregation-------------------------------------- */
        // eg. 按照价格区间[20]分组统计个数
        NativeSearchQueryBuilder histogramNativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //区间分桶
        HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram("price_histogram").field("id").interval(20);
        histogramNativeSearchQueryBuilder.withAggregations(histogramAggregationBuilder);
        // 查询
        SearchHits<TProjectIndex> searchHits = elasticsearchRestTemplate.search(histogramNativeSearchQueryBuilder.build(), TProjectIndex.class);
        // 结果处理
        if (searchHits.getAggregations() ==null) {
            return;
        }
        Aggregations aggregations = (Aggregations)searchHits.getAggregations().aggregations();
        List<Map<String, Object>> histogramDataGroup = ((ParsedHistogram) aggregations.get("price_histogram"))
                .getBuckets()
                .stream()
                .map(bucket -> {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("key", bucket.getKey());
                    map.put("count", bucket.getDocCount());
                    return map;
                })
                .collect(Collectors.toList());
        log.info("termsGroupData = {}", histogramDataGroup);
    }

    /**
     * 【聚合查询】
     *
     * 【Date Histogram Aggregation】：根据日期阶梯分组，例如给定阶梯为周，会自动每周分为一组
     *
     */
    @Test
    public void testDateHistogramAggregationSearch() {

        /* --------------------------------------Date Histogram Aggregation-------------------------------------- */
        NativeSearchQueryBuilder dateHistogramNativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 按年月份进行分组
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("createTime_histogram")
                .field("createTime")
                .calendarInterval(DateHistogramInterval.MONTH)
                .minDocCount(1);
        dateHistogramNativeSearchQueryBuilder.withAggregations(dateHistogramAggregationBuilder);
        SearchHits<TProjectIndex> searchHits = elasticsearchRestTemplate.search(dateHistogramNativeSearchQueryBuilder.build(), TProjectIndex.class);
        if (searchHits.getAggregations() == null) {
            return;
        }
        Aggregations aggregations = (Aggregations)searchHits.getAggregations().aggregations();
        List<Map<String, Object>> dateHistogramGroupData = ((ParsedDateHistogram) aggregations.get("createTime_histogram"))
                .getBuckets()
                .stream()
                .map(bucket -> {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("key", bucket.getKey());
                    map.put("count", bucket.getDocCount());
                    return map;
                })
                .collect(Collectors.toList());
        log.info("dateHistogramGroupData = {}", dateHistogramGroupData);
    }

    /**
     * 【聚合查询】
     *
     * 【Range Aggregation】：数值和日期的[范围]分组，指定开始和结束，然后按段分组
     *
     */
    @Test
    public void testRangeAggregationSearch() {

        /* --------------------------------------Range Aggregation-------------------------------------- */
        // eg. 指定数值范围200-300、时间范围
        NativeSearchQueryBuilder rangeNativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //范围分桶
        RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range("price_range").field("id").addRange(200, 300);
        rangeAggregationBuilder.subAggregation(AggregationBuilders.dateRange("date_range").field("createTime")
                        .format(DatePattern.NORM_DATETIME_PATTERN)
                .addRange("2010-01-01 00:00:00", "2020-01-01 00:00:00").addRange("2020-01-01 00:00:00", "2022-01-01 00:00:00"));
        rangeNativeSearchQueryBuilder.withAggregations(rangeAggregationBuilder);
        // 查询
        SearchHits<TProjectIndex> searchHits = elasticsearchRestTemplate.search(rangeNativeSearchQueryBuilder.build(), TProjectIndex.class);
        // 结果处理
        if (searchHits.getAggregations() ==null) {
            return;
        }
        Aggregations aggregations = (Aggregations)searchHits.getAggregations().aggregations();
        List<Map<String, Object>> rangeDataGroup = ((ParsedRange) aggregations.get("price_range"))
                .getBuckets()
                .stream()
                .map(bucket -> {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("key", bucket.getKey());
                    map.put("count", bucket.getDocCount());
                    if (bucket.getAggregations() != null) {
                        List<Map<String, Object>> subDateRange = ((ParsedDateRange) bucket.getAggregations().get("date_range"))
                                .getBuckets()
                                .stream()
                                .map(bucket1 -> {
                                    Map<String, Object> map1 = Maps.newHashMap();
                                    map1.put("key", bucket1.getKey());
                                    map1.put("count", bucket1.getDocCount());
                                    return map1;
                                })
                                .collect(Collectors.toList());
                        map.put("subDateRange", subDateRange);
                    }
                    return map;
                })
                .collect(Collectors.toList());
        log.info("rangeDataGroup = {}", rangeDataGroup);
    }

}
