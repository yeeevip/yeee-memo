package vip.yeee.memo.demo.blockchain.hyperchain.biz.service;

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
import cn.hyperchain.sdk.response.contract.StringResponse;
import cn.hyperchain.sdk.service.AccountService;
import cn.hyperchain.sdk.service.ContractService;
import cn.hyperchain.sdk.service.ServiceManager;
import cn.hyperchain.sdk.transaction.Transaction;
import vip.yeee.memo.demo.blockchain.hyperchain.biz.bo.BlockchainAccountBO;
import vip.yeee.memo.demo.blockchain.hyperchain.biz.bo.NFTPropertyMetaDataBO;
import vip.yeee.memo.demo.blockchain.hyperchain.biz.bo.NftIssueBO;
import vip.yeee.memo.demo.blockchain.hyperchain.biz.config.ChainProperties;
import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.base.util.JacksonUtils;

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
    private static Account deployAccount = null;

    @PostConstruct
    public void init() {
        // 初始化趣链SDK
        DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(chainProperties.getDefaultUrl()).build();
        ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
        contractService = ServiceManager.getContractService(providerManager);
        accountService = ServiceManager.getAccountService(providerManager);
        deployAccount = accountService.fromAccountJson(chainProperties.getDeployAccountJson());
    }

    public NftIssueBO issue(NFTPropertyMetaDataBO metaDataBO) throws Exception {
        if (StrUtil.isBlank(metaDataBO.getId())) {
            throw new Exception("id不能为空");
        }
        try {
            InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams
                    .ParamBuilder("issue")
                    .addString(deployAccount.getAddress())
                    .addString(metaDataBO.getId())
                    //.addString(JacksonUtils.toJsonString(metaDataBO))
                    .build();
            String res = invokeContract(deployAccount, invokeDirectlyParams, String.class, chainProperties.getContractAddress());
            return new NftIssueBO().setTokenId(res).setAccountBO(new BlockchainAccountBO().setAddress(deployAccount.getAddress()));
        } catch (Exception e) {
            log.error("趣链区块链：藏品上链失败，metaData = {}", metaDataBO, e);
            throw new Exception("藏品上链失败");
        }
    }

    public void transfer(String target, String nftId, NFTPropertyMetaDataBO metaDataBO) throws Exception {
        if (StrUtil.isBlank(target) || StrUtil.isBlank(nftId)) {
            throw new Exception("target和nftId不能为空");
        }
        try {
            Account fromAccount = deployAccount;
            InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams.ParamBuilder("transfer")
                    .addString(nftId)
                    .addString(fromAccount.getAddress())
                    .addString(target)
                    .addString(JacksonUtils.toJsonString(metaDataBO))
                    .build();
            boolean res = invokeContract(fromAccount, invokeDirectlyParams, Boolean.class, chainProperties.getContractAddress());
            if (!res) {
                throw new Exception("smart contract return false");
            }
        } catch (Exception e) {
            log.error("趣链区块链：藏品链上转移失败，nftId = {}， metaData = {}", nftId, metaDataBO, e);
            throw new Exception("藏品链上转移失败");
        }
    }

    public BlockchainAccountBO transfer(String nftId, NFTPropertyMetaDataBO metaDataBO) throws Exception {
        if (StrUtil.isBlank(nftId)) {
            throw new Exception("nftId不能为空");
        }
        try {
            Account fromAccount = deployAccount;
            Account targetAccount = accountService.genAccount(Algo.SMRAW);

            InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams.ParamBuilder("transfer")
                    .addString(nftId)
                    .addString(fromAccount.getAddress())
                    .addString(targetAccount.getAddress())
                    .addString(JacksonUtils.toJsonString(metaDataBO))
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
        log.info("趣链区块链：交易返回信息 -> {}", receipt.toString());
        return Decoder.decodeHVM(receipt.getRet(), clazz);
    }

    public void checkChainNodeServer() throws Exception {
        try {
            StringResponse response = contractService.getStatus(chainProperties.getContractAddress()).send();
            String res = response.getResult();
            if (!"normal".equals(res)) {
                log.error("趣链区块链节点状态异常，res = {}", res);
                throw new Exception("节点状态异常");
            }
        } catch (Exception e) {
            throw new Exception("区块链节点异常：" + e.getMessage());
        }
    }

}
