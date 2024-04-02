package vip.yeee.memo.demo.springboot.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/11/30 14:30
 */
@Component
public class TransactionExample {

    @Autowired
    @Qualifier("mysqlTransactionTemplate")
    private TransactionTemplate transactionTemplate;

    public void doSomething() {
//        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus status) {
                return null;
            }

        });
//        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
    }

}
