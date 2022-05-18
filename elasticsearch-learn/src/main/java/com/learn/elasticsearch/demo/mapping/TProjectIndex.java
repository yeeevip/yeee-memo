package com.learn.elasticsearch.demo.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 23:03
 */
@Document(indexName = "cf_project_2")
@Data
@EqualsAndHashCode(callSuper = true)
public class TProjectIndex extends BaseIndex implements TProjectIndexField {

    @Field(type = FieldType.Keyword, name = CATEGORY_ID)
    private Integer categoryId;
    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;
    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard", name = CONTENT)
    private String blurb;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss", name = CREATE_TIME)
    private LocalDateTime createTime;
}
