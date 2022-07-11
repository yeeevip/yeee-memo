package vip.yeee.memo.integrate.elasticsearch.service.impl;

import vip.yeee.memo.integrate.elasticsearch.mapping.BaseIndex;
import vip.yeee.memo.integrate.elasticsearch.service.ElasticsearchTemplate;
import vip.yeee.memo.integrate.elasticsearch.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean createIndex(Class<?> clazz) {
        return restTemplate.indexOps(clazz).create();
    }

    @Override
    public boolean exists(Class<?> clazz) {
        return restTemplate.indexOps(clazz).exists();
    }

    @Override
    public long count(QueryBuilder queryBuilder, String... indices) {
        return 0;
    }

    @Override
    public void delete(Class<?> clazz) {
        restTemplate.indexOps(clazz).delete();
    }

    @Override
    public List<String> bulk(List<? extends BaseIndex> list, String indexName) {
        List<IndexQuery> queries = list.stream().map(item -> new IndexQueryBuilder().withObject(item).build()).collect(Collectors.toList());
        return restTemplate.bulkIndex(queries, IndexCoordinates.of(indexName));
    }

    @Override
    public <T> PageVO<T> pageSearch(Integer pageNum, Integer pageSize, QueryBuilder queryBuilder, Class<T> index) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageRequest)
                .withSort(sortBuilder)
                .build();
        List<SearchHit<T>> searchHits = restTemplate.search(nativeSearchQuery, index).getSearchHits();
        PageVO<T> pageVO = new PageVO<>(pageNum, pageSize);
        pageVO.setResult(searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList()));
        return pageVO;
    }

    @Override
    public <T> Aggregations aggregationSearch(TermsAggregationBuilder aggregationBuilder, Class<T> index) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .addAggregation(aggregationBuilder)
                .build();
        return restTemplate.search(nativeSearchQuery, index).getAggregations();
    }

}
