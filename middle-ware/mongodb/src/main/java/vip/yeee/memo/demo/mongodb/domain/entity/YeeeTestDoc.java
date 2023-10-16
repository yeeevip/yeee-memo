package vip.yeee.memo.demo.mongodb.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("yeee_test_doc")
@Data
public class YeeeTestDoc {

    @Id
    private String id;

    private String content;

    private Date createTime;

    private Date updateTime;

}
