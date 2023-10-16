package vip.yeee.memo.demo.mongodb.domain.repository;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.mongodb.domain.entity.YeeeTestDoc;
import vip.yeee.memo.demo.mongodb.utils.BusinessUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/10/16 15:18
 */
@Slf4j
@Component
public class YeeeTestDocRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    public YeeeTestDoc saveDoc() {
        YeeeTestDoc saveModel = new YeeeTestDoc();
        saveModel.setId(IdUtil.simpleUUID());
        saveModel.setContent("");
        saveModel.setCreateTime(new Date());
        saveModel.setUpdateTime(new Date());
        YeeeTestDoc res = mongoTemplate.insert(saveModel);
        return res;
    }

    public void updateDocById(YeeeTestDoc yeeeTestDoc) {
        Query query = new Query(Criteria.where("id").is(yeeeTestDoc.getId()));
        Update update = BusinessUtils.buildMongoUpdate(yeeeTestDoc);
        long modifiedCount = mongoTemplate.update(YeeeTestDoc.class)
                .matching(query)
                .apply(update)
                .upsert()
                .getModifiedCount();
        log.info("updateDocById modifiedCount = {}", modifiedCount);
    }
}
