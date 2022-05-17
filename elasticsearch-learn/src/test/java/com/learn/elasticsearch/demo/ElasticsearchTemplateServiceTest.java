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
import java.util.ArrayList;
import java.util.List;

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
    public void testDelIndex() throws Exception {
        elasticsearchTemplate.delete(MyIndex.class);
        log.error("---------------del----------------");
    }

    @Test
    public void testExists() throws Exception {
        boolean exists = elasticsearchTemplate.exists(MyIndex.class);
        log.error("---------------exists = {}----------------", exists);
    }

    @Test
    public void testBulk() throws IOException {
        List<MyIndex> myIndexList = new ArrayList<>();

        MyIndex myIndex1 = new MyIndex();
        myIndex1.setId("1");
        myIndex1.setTitle("好的送你发生了发撒的积分撒地方撒多了几分按时了解到复联三加多少分");
        myIndex1.setContent("萨迪克广东省考姐夫撒娇发射基地加多少分看见打上来看附件ADSL附件阿斯利康加上费德里科加多少就");
        myIndexList.add(myIndex1);

        MyIndex myIndex2 = new MyIndex();
        myIndex2.setId("2");
        myIndex2.setTitle("好的送你发生了发撒的积分撒地方撒多了几分按时了解到复联三加多少分");
        myIndex2.setContent("萨迪克广东省考姐夫撒娇发射基地加多少分看见打上来看附件ADSL附件阿斯利康加上费德里科加多少就");
        myIndexList.add(myIndex2);

        MyIndex myIndex3 = new MyIndex();
        myIndex3.setId("3");
        myIndex3.setTitle("好的送你发生了发撒的积分撒地方撒多了几分按时了解到复联三加多少分");
        myIndex3.setContent("萨迪克广东省考姐夫撒娇发射基地加多少分看见打上来看附件ADSL附件阿斯利康加上费德里科加多少就");
        myIndexList.add(myIndex3);

        elasticsearchTemplate.bulk(myIndexList, "my_index", "");
    }

}
