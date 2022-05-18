package com.learn.elasticsearch.demo.repository;

import com.learn.elasticsearch.demo.mapping.TProjectIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/18 10:09
 */
@Repository
public interface TProjectIndexRepository extends ElasticsearchRepository<TProjectIndex, Integer> {
}
