package vip.yeee.memo.integrate.elasticsearch;

import org.elasticsearch.common.unit.Fuzziness;
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

    @Test
    public void testExistsIndex() {
        boolean flag = elasticsearchRestTemplate.indexOps(TProjectIndex.class).exists();
        log.info("flag = {}", flag);
    }

    @Test
    public void testDeleteIndex() {
        boolean flag = elasticsearchRestTemplate.indexOps(TProjectIndex.class).delete();
        log.info("flag = {}", flag);
    }

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
     * 比较常用的一些度量聚合方式：
     *
     * Avg Aggregation：求平均值
     * Max Aggregation：求最大值
     * Min Aggregation：求最小值
     * Percentiles Aggregation：求百分比
     * Stats Aggregation：同时返回avg、max、min、sum、count等
     * Sum Aggregation：求和
     * Top hits Aggregation：求前几
     * Value Count Aggregation：求总数
     *
     */
    @Test
    public void aggregationSearch() {



    }

}
