package vip.yeee.memo.integrate.elasticsearch;

import vip.yeee.memo.integrate.elasticsearch.opr.ITProjectService;
import vip.yeee.memo.integrate.elasticsearch.opr.TProject;
import vip.yeee.memo.integrate.elasticsearch.mapping.TProjectIndex;
import vip.yeee.memo.integrate.elasticsearch.repository.TProjectIndexRepository;
import vip.yeee.memo.integrate.elasticsearch.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class ElasticsearchRepositoryTest {

    @Autowired
    private TProjectIndexRepository projectIndexRepository;
    @Autowired
    private ITProjectService iTProjectService;

    @Test
    public void testBulk() {
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
        Iterable<TProjectIndex> res = projectIndexRepository.saveAll(myIndexList);
        log.info("-------------bulk res = {}------------------", res);
    }

    @Test
    public void testConditionPageSearch() {
        Integer pageNum = 1, pageSize = 20;
        String keyword = "北京";
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("id").gt(1));
        queryBuilder.must(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhraseQuery("title", keyword))
                .should(QueryBuilders.matchPhraseQuery("content", keyword)));
        Page<TProjectIndex> page = projectIndexRepository.search(queryBuilder, PageRequest.of(pageNum - 1, pageSize));
        PageVO<TProjectIndex> pageVO = new PageVO<>(page.getNumber(), page.getSize());
        pageVO.setResult(page.getContent());
        log.info("--------------page = {}-----------------", pageVO);
    }

}
