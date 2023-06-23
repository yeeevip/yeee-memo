package vip.yeee.memo.demo.design.practice.statics.vo;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/3 13:40
 */
@Data
public class StaticDataVo<T> {

    private String timeStamp;

    private String siteName;

    private T data;

}
