package vip.yeee.memo.base.model.vo;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/30 12:54
 */
@Data
public class PageReqVO<T> {

    private Integer pageNum = 1;

    private Integer pageSize = 20;

    private T query;

}
