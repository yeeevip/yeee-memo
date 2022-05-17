package com.yeeee.chain.biz.bo;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/16 16:52
 */
@Data
public class NFTPropertyMetaDataBO {

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 发行方
     */
    private String issuer;

    /**
     * oss地址
     */
    private String uri;

}
