package ${package};

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
* create by yeee.一页 ${generateDate}
*/
@Data
public class IdRequest {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("IDS")
    private List<Long> ids;

}