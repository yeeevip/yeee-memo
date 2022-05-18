package com.learn.elasticsearch.demo;

import com.learn.ElasticsearchApplication;
import com.learn.db.opr.ITProjectService;
import com.learn.db.opr.TProject;
import com.learn.elasticsearch.demo.mapping.TProjectIndex;
import com.learn.elasticsearch.demo.service.ElasticsearchTemplate;
import lombok.extern.slf4j.Slf4j;
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
