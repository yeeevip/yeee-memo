package vip.yeee.memo.integrate.elasticsearch.repository;

import vip.yeee.memo.integrate.elasticsearch.mapping.TProjectIndex;
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
