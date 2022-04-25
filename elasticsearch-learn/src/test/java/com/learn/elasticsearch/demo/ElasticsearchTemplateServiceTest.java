package com.learn.elasticsearch.demo;

import com.learn.elasticsearch.demo.mapping.MyIndex;
import com.learn.elasticsearch.demo.service.ElasticsearchService;
import com.learn.elasticsearch.demo.service.ElasticsearchTemplate;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

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

    @Test
    public void testCreateIndex() throws IOException {
        boolean res = elasticsearchTemplate.createIndex(MyIndex.class);
        log.error("---------------res = {}----------------", res);
    }

    @Test
    public void testExists() throws Exception {
        boolean exists = elasticsearchTemplate.exists(MyIndex.class);
        log.error("---------------exists = {}----------------", exists);
    }

}
