package com.learn.elasticsearch.demo;

import com.learn.ElasticsearchApplication;
import com.learn.db.opr.ITProjectService;
import com.learn.db.opr.TProject;
import com.learn.elasticsearch.demo.mapping.TProjectIndex;
import com.learn.elasticsearch.demo.repository.TProjectIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

}
