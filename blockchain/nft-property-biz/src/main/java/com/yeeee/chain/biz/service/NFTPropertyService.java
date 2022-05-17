package com.yeeee.chain.biz.service;

import com.yeeee.chain.biz.bo.BlockchainAccountBO;
import com.yeeee.chain.biz.bo.NFTPropertyMetaDataBO;

/**
 * 数字藏品区块链-sdk
 *
 * @author yeeee
 * @since 2022/5/16 16:48
 */
public interface NFTPropertyService {

    // 智能合约-发行方法
    String CONTRACT_ISSUE = "issue";

    // 智能合约-资产转移方法
    String CONTRACT_TRANSFER = "transfer";

    /**
     * 发行数字藏品 -> 发行方
     */
    void issue(NFTPropertyMetaDataBO metaDataBO) throws Exception;

    /**
     * 数字藏品转移，from（发行方） -> to（user）
     */
    BlockchainAccountBO transfer(String nftId) throws Exception;

}
