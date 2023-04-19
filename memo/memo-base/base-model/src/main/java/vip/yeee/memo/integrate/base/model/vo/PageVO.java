package vip.yeee.memo.integrate.base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/30 12:54
 */
@Data
@AllArgsConstructor
public class PageVO<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Integer pages;

    private Long total;

    private Collection<T> result;

}
