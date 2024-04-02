package vip.yeee.memo.demo.blockchain.hyperchain.contract;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
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
import org.junit.Test;

import java.io.InputStream;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/5/11 15:41
 */
public class NFTPropertyTest {

    public static String defaultURL = "192.168.23.123:8081";
    public static String jarPath = System.getProperty("user.dir") + "/target/hyperchain-nft-property-1.0-property.jar";
    public static DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(defaultURL).build();
    public static ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);

    public static ContractService contractService = ServiceManager.getContractService(providerManager);
    public static AccountService accountService = ServiceManager.getAccountService(providerManager);

    String deployAccountJson = "{\"address\":\"3c219494caf1cad1f50f6fca592e29edc416c3d6\",\"publicKey\":\"04c079d9a01311ae4fb59abf6a29575386ccb308f2a2edca9d58deaa77f8b3fd1f4090a3c4614dea29290943b153d27cc9e58d509826c936c574b58daf21ddc8c0\",\"privateKey\":\"84531735251405b7ddcd98b03af415cc45129ddec00bfb41434f289e282401d7\",\"version\":\"V4\",\"algo\":\"0x13\"}";
    Account deployAccount = accountService.fromAccountJson(deployAccountJson);

    static String contractAddress = "0xeacf4b079c168338380a58b97cfbadfa5394859b";

    /**
     * 创建区块链账号
     */
    @Test
    public void testGenAccount() {
        final String ACCOUNT_JSON_TEMPLATE = "{\"address\":\"%s\",\"publicKey\":\"%s\",\"privateKey\":\"%s\",\"version\":\"%s\",\"algo\":\"%s\"}";
        Account newAccount = accountService.genAccount(Algo.SMRAW);
        System.out.printf(ACCOUNT_JSON_TEMPLATE
                , newAccount.getAddress()
                , newAccount.getPublicKey()
                , newAccount.getPrivateKey()
                , newAccount.getVersion()
                , newAccount.getAlgo().getAlgo());
    }

    /**
     * 部署智能合约
     */
    @Test
    public void deployContract() throws Exception {

        InputStream is = FileUtil.readFileAsStream(jarPath);
        Transaction transaction = new Transaction.HVMBuilder(deployAccount.getAddress()).deploy(is).build();
        transaction.sign(deployAccount);
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println(receiptResponse.getGasUsed());
        System.out.println("contract address: " + contractAddress);
    }

    /**
     * 发行数字藏品
     */
    @Test
    public void testIssue() throws Exception {

        String nftId = "10012";
        String metaData = "{\"id\":\"10012\",\"name\":\"NBA75周年纪念币\",\"issuer\":\"NBA官方\",\"uri\":\"http://t14.baidu.com/it/u=3100520023,4056026874&fm=224&app=112&f=JPEG?w=350&h=350\"}";

        InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams.ParamBuilder("issue")
                .addString(deployAccount.getAddress())
                .addString(nftId)
                .addString(metaData)
                .build();
        boolean ret = invokeContract(deployAccount, invokeDirectlyParams, Boolean.class, contractAddress);
        System.out.println(ret);
    }

    /**
     * 转移数字藏品给他人
     */
    @Test
    public void testTransfer() throws Exception {

        String nftId = "10012";
        Account fromAccount = deployAccount;
        String toAccountAddress = "be82dcef3323863799549aa26748eace53a7a124";

        InvokeDirectlyParams invokeDirectlyParams = new InvokeDirectlyParams.ParamBuilder("transfer")
                .addString(nftId)
                .addString(fromAccount.getAddress())
                .addString(toAccountAddress)
                .build();
        boolean ret = invokeContract(fromAccount, invokeDirectlyParams, Boolean.class, contractAddress);
        System.out.println(ret);
    }

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