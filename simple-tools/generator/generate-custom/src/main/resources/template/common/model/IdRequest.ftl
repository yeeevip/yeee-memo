package ${package};

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* create by yeee.一页 ${generateDate}
*/
@Data
public class IdRequest {

    @ApiModelProperty("ID")
    @NotNull(message = "ID不能为空")
    private Long id;

}