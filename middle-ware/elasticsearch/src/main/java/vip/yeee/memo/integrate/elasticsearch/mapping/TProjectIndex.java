package vip.yeee.memo.integrate.elasticsearch.mapping;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import vip.yeee.memo.integrate.elasticsearch.constant.EsIndex;

import java.time.LocalDateTime;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 23:03
 */
@Document(indexName = EsIndex.T_PROJECT2_INDEX)
@Data
@EqualsAndHashCode(callSuper = true)
public class TProjectIndex extends BaseIndex implements TProjectIndexField {

    // Keyword表示不使用分词
    @Field(type = FieldType.Keyword, name = CATEGORY_ID)
    private Integer categoryId;
    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;
    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard", name = CONTENT)
    private String blurb;
//    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Field(type = FieldType.Date, format = {}, pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
}
