package ${package};

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* create by yeee.一页 ${generateDate}
*/
@Data
public class IdRequest {

    @ApiModelProperty("ID")
    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty("IDS")
    @NotEmpty(message = "IDS不能为空")
    private List<Long> ids;

}