package vip.yeee.memo.integrate.elasticsearch;

import vip.yeee.memo.integrate.elasticsearch.ElasticsearchApplication;
import vip.yeee.memo.integrate.elasticsearch.opr.ITProjectService;
import vip.yeee.memo.integrate.elasticsearch.opr.TProject;
import vip.yeee.memo.integrate.elasticsearch.mapping.TProjectIndex;
import vip.yeee.memo.integrate.elasticsearch.service.ElasticsearchTemplate;
import vip.yeee.memo.integrate.elasticsearch.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchTemplateServiceTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ITProjectService iTProjectService;

    @Test
    public void testCreateIndex() throws Exception {
        boolean exists = elasticsearchTemplate.exists(TProjectIndex.class);
        log.info("-----------------exists = {}---------------------", exists);
        if (!exists) {
            boolean create = elasticsearchTemplate.createIndex(TProjectIndex.class);
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
        List<String> res = elasticsearchTemplate.bulk(myIndexList, "cf_project_2");
        log.info("-------------bulk res = {}------------------", res);
    }

    /**
     * //进行单个值得精确匹配termQuery("key", obj) 完全匹配
     * QueryBuilders.termQuery("name","我是中国人")
     *
     * //进行模糊查询multiMatchQuery("key.keyword", "*field1*"); 可以使用通配符.keyword是否添加查看上方的    Es的常见问题
     * QueryBuilders.wildcardQuery("name"/"name.keyword","*我是中*")
     *
     * //进行多个值得精确匹配termsQuery("key", obj1, obj2..)   一次匹配多个值
     * String[] a = {"a","ab","ac","qq"}
     * QueryBuilders.termsQuery("name",a)
     *
     * //模糊查询  和wildcardQuery对比的话不能使用通配符
     * QueryBuilders.matchQuery("name","我是")
     *
     * //matchPhraseQuery对中文精确匹配
     * queryBuilders.matchPhraseQuery("key", value)
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
        PageVO<TProjectIndex> pageVO = elasticsearchTemplate.pageSearch(1, 20, queryBuilder, TProjectIndex.class);
        log.info("---------pageVO = {}-----------", pageVO);
    }

    @Test
    public void testAggregationSearch() {
        // 按categoryId 分组group by，并在各分组内按月统计
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("category").field("categoryId")
                .subAggregation(AggregationBuilders.dateHistogram("createDate").field("createTime").calendarInterval(DateHistogramInterval.MONTH).minDocCount(1))
                .size(500);
        Aggregations aggregations = elasticsearchTemplate.aggregationSearch(aggregationBuilder, TProjectIndex.class);

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
        boolean exists = elasticsearchTemplate.exists(TProjectIndex.class);
        log.info("---------------exists = {}----------------", exists);
    }

    @Test
    public void testDelIndex() throws Exception {
        elasticsearchTemplate.delete(TProjectIndex.class);
        log.info("---------------del----------------");
    }

}
