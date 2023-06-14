package ${package};

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
<#list tableClass.allFields as field>
<#if field.shortTypeName == 'Date'>
import com.fasterxml.jackson.annotation.JsonFormat;
<#break>
</#if>
</#list>

import lombok.Data;

/**
* create by yeee.一页 ${generateDate}
*/
@Data
public class ${tableClass.shortClassName}InfoVo {

<#list tableClass.allFields as field>
    @ApiModelProperty("${field.remarks}")
    <#if field.shortTypeName == 'Date'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if field.shortTypeName == 'Long'>
    private String ${field.fieldName};
    <#else>
    private ${field.shortTypeName} ${field.fieldName};
    </#if>

</#list>
}