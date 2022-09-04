package vip.yeee.memo.integrate.elasticsearch.service;

import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import vip.yeee.memo.integrate.elasticsearch.mapping.BaseIndex;
import vip.yeee.memo.integrate.elasticsearch.vo.PageVO;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.io.IOException;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:58
 */
public interface ElasticsearchTemplate {

    /**
     * 创建索引
     */
    boolean createIndex(Class<?> clazz) throws IOException;

    /**
     * 判断索引是否存在
     */
    boolean exists(Class<?> clazz) throws Exception;

    /**
     * 统计数量
     */
    long count(QueryBuilder queryBuilder, String... indices) throws Exception;

    /**
     * 删除索引
     */
    void delete(Class<?> clazz) throws Exception;

//    /**
//     * 批量保存
//     */
//    List<String> bulk(List<? extends BaseIndex> list, String indexName) throws IOException;

    /**
     * 批量保存
     */
    List<IndexedObjectInformation> bulk(List<? extends BaseIndex> list, String indexName) throws IOException;

    /**
     * 批量保存
     */
    Iterable<? extends BaseIndex> saveBatch(List<? extends BaseIndex> list) throws IOException;

    /**
     * 分页条件搜索
     */
    <T> PageVO<T> pageSearch(Integer pageNum, Integer pageSize, QueryBuilder queryBuilder, Class<T> index);

    /**
     * 聚合查询
     */
    <T> Aggregations aggregationSearch(TermsAggregationBuilder aggregationBuilder, Class<T> index);

}
