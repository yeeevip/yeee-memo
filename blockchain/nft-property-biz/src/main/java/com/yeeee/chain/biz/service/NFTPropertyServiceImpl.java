package com.yeeee.chain.biz.service;

import cn.hutool.core.util.StrUtil;
import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.InvokeDirectlyParams;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.TxHashResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import com.alibaba.fastjson.JSON;
import com.yeeee.chain.biz.bo.BlockchainAccountBO;
import com.yeeee.chain.biz.bo.NFTPropertyMetaDataBO;
import com.yeeee.chain.biz.config.ChainProperties;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/16 19:03
 */
@Slf4j
//@Service
public class NFTPropertyServiceImpl implements NFTPropertyService {

    @Resource
    private ChainProperties chainProperties;
    private static ContractService contractService = null;
    private static AccountService accountService = null;

    @PostConstruct
    public void init() {
        // 初始化趣链SDK
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(chainProperties.getDefaultUrl()).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        contractService = ServiceManager.getContractService(providerManager);
        accountService = ServiceManager.getAccountService(providerManager);
    }

    @Override
    public void issue(NFTPropertyMetaDataBO metaDataBO) throws Exception {
        if (StrUtil.hasBlank(metaDataBO.getId(), metaDataBO.getName(), metaDataBO.getIssuer(), metaDataBO.getUri())) {
            throw new Exception("metaDataBO参数不全");
        }
        try {
            Account deployAccount = accountService.fromAccountJson(chainProperties.getDeployAccountJson());
            InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams
                    .ParamBuilder(CONTRACT_ISSUE)
                    .addString(deployAccount.getAddress())
                    .addString(metaDataBO.getId())
                    .addString(JSON.toJSONString(metaDataBO))
                    .build();
            boolean res = invokeContract(deployAccount, invokeDirectlyParams, Boolean.class, chainProperties.getContractAddress());
            if (!res) {
                throw new Exception("smart contract return false");
            }
        } catch (Exception e) {
            log.error("趣链区块链：发行藏品失败，metaData = {}", metaDataBO, e);
            throw new Exception("发行藏品失败");
        }
    }

    @Override
    public BlockchainAccountBO transfer(String nftId) throws Exception {
        if (StrUtil.isBlank(nftId)) {
            throw new Exception("nftId不能为空");
        }
        try {
            Account fromAccount = accountService.fromAccountJson(chainProperties.getDeployAccountJson());
            Account targetAccount = accountService.genAccount(Algo.SMRAW);

            InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams.ParamBuilder(CONTRACT_TRANSFER)
                    .addString(nftId)
                    .addString(fromAccount.getAddress())
                    .addString(targetAccount.getAddress())
                    .build();
            boolean res = invokeContract(fromAccount, invokeDirectlyParams, Boolean.class, chainProperties.getContractAddress());
            if (!res) {
                throw new Exception("smart contract return false");
            }
            return new BlockchainAccountBO()
                    .setAddress(targetAccount.getAddress())
                    .setPublicKey(targetAccount.getPublicKey()).setPrivateKey(targetAccount.getPrivateKey())
                    .setVersion(targetAccount.getVersion().getV()).setAlgo(targetAccount.getAlgo().getAlgo());
        } catch (Exception e) {
            log.error("趣链区块链：数字藏品转移失败，nftId = {}", nftId, e);
            throw new Exception("数字藏品转移失败");
        }
    }

    /**
     * 调用合约定义的方法
     */
    public <T> T invokeContract(Account from, InvokeDirectlyParams param, Class<T> clazz, String contractAddress, String... tags) throws RequestException {
        Transaction transaction = new Transaction.HVMBuilder(from.getAddress())
                .invokeDirectly(contractAddress, param)
                .extraIDString(tags)
                .build();
        transaction.sign(from);
        TxHashResponse send = contractService.invoke(transaction).send();
        ReceiptResponse receipt = send.polling();
        return Decoder.decodeHVM(receipt.getRet(), clazz);
    }

}
