package com.yeeee.property.api;

import cn.hyperchain.contract.BaseContractInterface;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/16 13:37
 */
public interface NFTPropertyService extends BaseContractInterface {

    /**
     * 发行数字藏品
     */
    boolean issue(String ownerAddr, String id, String metaData);

    /**
     * 转移数字藏品
     */
    boolean transfer(String id, String fromAddr, String toAddr);

}
