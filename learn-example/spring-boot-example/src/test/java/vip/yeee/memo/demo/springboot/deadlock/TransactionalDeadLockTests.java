package vip.yeee.memo.demo.springboot.deadlock;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import tk.mybatis.mapper.entity.Example;
import vip.yeee.memo.demo.springboot.domain.mysql.entity.TempTest1;
import vip.yeee.memo.demo.springboot.domain.mysql.mapper.TempTest1Mapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/2/22 17:18
 */
@Slf4j
@SpringBootTest
public class TransactionalDeadLockTests {

    @Resource
    private TempTest1Mapper tempTest1Mapper;
    @Autowired
    @Qualifier("mysqlTransactionTemplate")
    private TransactionTemplate transactionTemplate;

    private final CountDownLatch countDownLatch = new CountDownLatch(2);

    @Test
    public void testTransDeadLock() throws InterruptedException {
        List<TempTest1> tempTest1List = Lists.newArrayList();
        tempTest1List.add(new TempTest1(IdUtil.simpleUUID(), 1, "111"));
        tempTest1List.add(new TempTest1(IdUtil.simpleUUID(), 1, "222"));
        new Thread(() -> this.execBizHandle(tempTest1List)).start();
        List<TempTest1> tempTest1List2 = Lists.newArrayList();
        tempTest1List2.add(new TempTest1(IdUtil.simpleUUID(), 1, "222"));
        tempTest1List2.add(new TempTest1(IdUtil.simpleUUID(), 1, "111"));
        new Thread(() -> this.execBizHandle(tempTest1List2)).start();
        countDownLatch.await();
    }

    private void execBizHandle(List<TempTest1> tempTest1List) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus status) {
                try {
                    for (TempTest1 tempTest1 : tempTest1List) {
                        Example example = Example.builder(TempTest1.class).build();
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("parentId", tempTest1.getParentId());
                        TempTest1 po = tempTest1Mapper.selectOneByExample(example);
                        log.info("------------ po = {} -----------", po);
                        if (po != null) {
                            po.setCount(tempTest1.getCount() + po.getCount());
                            int res = tempTest1Mapper.updateByPrimaryKeySelective(po);
                            log.info("------------ upd = {} -----------", res);
                        } else {
                            int res = tempTest1Mapper.insert(tempTest1);
                            log.info("------------ add = {} -----------", res);
                        }
                    }
//                    try {
//                        TimeUnit.SECONDS.sleep(2);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } catch (Exception e) {
                    log.error("------------ error -----------", e);
                }
                return null;
            }

        });
        countDownLatch.countDown();
    }
}
