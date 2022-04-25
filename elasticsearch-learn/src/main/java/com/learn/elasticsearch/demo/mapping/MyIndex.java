package com.learn.elasticsearch.demo.mapping;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 23:03
 */
@Document(indexName = "my_index")
@Data
public class MyIndex implements MyIndexField {

    @Id
    private String id;
    private String overdueTime;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss", name = CREATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss", name = UPDATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

}
