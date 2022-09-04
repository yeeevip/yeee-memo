package vip.yeee.memo.integrate.elasticsearch.service;

import vip.yeee.memo.integrate.elasticsearch.vo.PageVO;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 21:57
 */
public interface ElasticsearchService {

    /**
     * 创建索引
     */
    boolean createIndex(String indexName, String mapping) throws IOException;

    /**
     * 判断索引是否存在
     */
    boolean exists(String indexName) throws Exception;

    /**
     * 统计数量
     */
    long count(QueryBuilder queryBuilder, String... indices) throws Exception;

    /**
     * 删除索引
     */
    void delete(String indexName) throws Exception;

    /**
     * 批量保存
     */
    BulkResponse bulk(String index, List<Map<String, Object>> list) throws Exception;

    /**
     * 分页条件搜索
     */
    PageVO<SearchHit> pageSearch(Integer pageNum, Integer pageSize, QueryBuilder queryBuilder, String... index) throws IOException;

    /**
     * 聚合查询
     */
    Aggregations aggregationSearch(AggregationBuilder aggregationBuilder, String... index) throws IOException;

}
