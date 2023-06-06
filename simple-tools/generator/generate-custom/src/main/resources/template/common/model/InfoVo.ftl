package ${package};

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

import lombok.Data;

/**
* create by yeee.一页 ${generateDate}
*/
@Data
public class ${tableClass.shortClassName}InfoVo {

<#list tableClass.allFields as field>
    @ApiModelProperty("${field.remarks}")
    private ${field.shortTypeName} ${field.fieldName};

</#list>
}