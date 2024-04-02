package vip.yeee.memo.demo.elasticsearch.vo;

import lombok.Data;

import java.util.Collection;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/4/30 12:54
 */
@Data
public class PageVO<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Integer pages;

    private Long total;

    private Collection<T> result;

    public PageVO(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
