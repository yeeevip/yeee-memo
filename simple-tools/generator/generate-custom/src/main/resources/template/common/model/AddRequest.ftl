package ${package};

import io.swagger.annotations.ApiModelProperty;
<#list tableClass.allFields as field>
<#if field.shortTypeName == 'Date'>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
<#break>
</#if>
</#list>

import lombok.Data;

/**
* create by yeee.一页 ${generateDate}
*/
@Data
public class ${tableClass.shortClassName}AddRequest {
<#list tableClass.allFields as field>
    <#if field.columnName != 'id'>
    @ApiModelProperty("${field.remarks}")
    <#if field.shortTypeName == 'Date'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    private ${field.shortTypeName} ${field.fieldName};
    </#if>

</#list>
}