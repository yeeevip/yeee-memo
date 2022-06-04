package com.yeeee.chain.biz.bo;

import lombok.Data;

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
     * 藏品HASH
     */
    private String entityHash;

    /**
     * oss地址
     */
    private String uri;

}
