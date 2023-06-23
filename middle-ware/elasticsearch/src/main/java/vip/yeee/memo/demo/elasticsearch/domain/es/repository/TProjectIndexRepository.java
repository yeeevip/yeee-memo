package vip.yeee.memo.demo.elasticsearch.domain.es.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import vip.yeee.memo.demo.elasticsearch.domain.es.entity.TProjectIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/18 10:09
 */
@Repository
public interface TProjectIndexRepository extends ElasticsearchRepository<TProjectIndex, Integer> {

    @Query("{\"bool\":{\"should\":[{\"query_string\":{\"query\":\"?0\",\"fields\":[\"title\"]}},{\"query_string\":{\"query\":\"?1\",\"fields\":[\"content\"]}}]}}")
    List<TProjectIndex> queryByTitleOrBlurbOrderByIdDesc(String title, String blurb);

    List<TProjectIndex> findByTitleOrBlurbOrderByIdDesc(String title, String blurb);

    Page<TProjectIndex> findByTitleOrBlurbOrderByIdDesc(String title, String blurb, Pageable pageable);

}
