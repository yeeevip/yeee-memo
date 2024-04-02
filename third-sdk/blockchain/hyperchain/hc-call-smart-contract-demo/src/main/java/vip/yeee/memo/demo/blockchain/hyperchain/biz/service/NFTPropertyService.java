package vip.yeee.memo.demo.blockchain.hyperchain.biz.service;

import vip.yeee.memo.demo.blockchain.hyperchain.biz.bo.BlockchainAccountBO;
import vip.yeee.memo.demo.blockchain.hyperchain.biz.bo.NFTPropertyMetaDataBO;
import vip.yeee.memo.demo.blockchain.hyperchain.biz.bo.NftIssueBO;

/**
 * 数字藏品区块链-sdk
 *
 * @author https://www.yeee.vip
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
    NftIssueBO issue(NFTPropertyMetaDataBO metaDataBO) throws Exception;

    /**
     * 数字藏品转移，from（发行方） -> to（user）
     */
    void transfer(String target, String nftId, NFTPropertyMetaDataBO metaDataBO) throws Exception;

    /**
     * 数字藏品转移，from（发行方） -> to（user）
     */
    BlockchainAccountBO transfer(String nftId, NFTPropertyMetaDataBO metaDataBO) throws Exception;

}
