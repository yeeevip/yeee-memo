package vip.yeee.memo.integrate.blockchain.ac.call;

import com.alipay.mychain.sdk.api.logging.AbstractLoggerFactory;
import com.alipay.mychain.sdk.api.logging.ILogger;
import com.alipay.mychain.sdk.api.utils.ConfidentialUtil;
import com.alipay.mychain.sdk.api.utils.Utils;
import com.alipay.mychain.sdk.common.VMTypeEnum;
import com.alipay.mychain.sdk.crypto.MyCrypto;
import com.alipay.mychain.sdk.crypto.keyoperator.Pkcs8KeyOperator;
import com.alipay.mychain.sdk.crypto.keypair.Keypair;
import com.alipay.mychain.sdk.crypto.signer.SignerBase;
import com.alipay.mychain.sdk.domain.account.Identity;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.alipay.mychain.sdk.api.MychainClient;
import com.alipay.mychain.sdk.api.env.ClientEnv;
import com.alipay.mychain.sdk.api.env.ISslOption;
import com.alipay.mychain.sdk.api.env.SignerOption;
import com.alipay.mychain.sdk.api.env.SslBytesOption;
import com.alipay.mychain.sdk.errorcode.ErrorCode;
import com.alipay.mychain.sdk.message.transaction.AbstractTransactionRequest;
import com.alipay.mychain.sdk.message.transaction.TransactionReceiptResponse;
import com.alipay.mychain.sdk.message.transaction.confidential.ConfidentialRequest;
import com.alipay.mychain.sdk.message.transaction.contract.CallContractRequest;
import com.alipay.mychain.sdk.message.transaction.contract.DeployContractRequest;
import com.alipay.mychain.sdk.type.BaseFixedSizeUnsignedInteger;
import com.alipay.mychain.sdk.utils.ByteUtils;
import com.alipay.mychain.sdk.utils.IOUtil;
import com.alipay.mychain.sdk.utils.RandomUtil;
import com.alipay.mychain.sdk.vm.EVMOutput;
import com.alipay.mychain.sdk.vm.EVMParameter;

public class DemoSample {
    /**
     * contract code
     */
    private static String contractCodeString = "0x608060405234801561001057600080fd5b5060405161063938038061063983398101806040528101908080518201929190505050806000908051906020019061004992919061005c565b50600080549050600281905550506100d4565b82805482825590600052602060002090810192821561009e579160200282015b8281111561009d57825182906000191690559160200191906001019061007c565b5b5090506100ab91906100af565b5090565b6100d191905b808211156100cd5760008160009055506001016100b5565b5090565b90565b610556806100e36000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630d15fd77146100885780632f265cf7146100b3578063392e6678146100fe5780637021939f14610147578063a9a981a314610192578063b13c744b146101bd578063cc9ab26714610206575b600080fd5b34801561009457600080fd5b5061009d610237565b6040518082815260200191505060405180910390f35b3480156100bf57600080fd5b506100e2600480360381019080803560001916906020019092919050505061023d565b604051808260ff1660ff16815260200191505060405180910390f35b34801561010a57600080fd5b5061012d60048036038101908080356000191690602001909291905050506102ec565b604051808215151515815260200191505060405180910390f35b34801561015357600080fd5b5061017660048036038101908080356000191690602001909291905050506103c3565b604051808260ff1660ff16815260200191505060405180910390f35b34801561019e57600080fd5b506101a76103e3565b6040518082815260200191505060405180910390f35b3480156101c957600080fd5b506101e8600480360381019080803590602001909291905050506103e9565b60405180826000191660001916815260200191505060405180910390f35b34801561021257600080fd5b50610235600480360381019080803560001916906020019092919050505061040c565b005b60035481565b6000610248826102ec565b15156102bc576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f63616e64696461746520697320696e76616c696400000000000000000000000081525060200191505060405180910390fd5b60016000836000191660001916815260200190815260200160002060009054906101000a900460ff169050919050565b600080600090505b60008054905081101561037c57826000191660008281548110151561031557fe5b906000526020600020015460001916141561036f577f2b766bfa48dbb99822ac647fffc163dc74b7857beedb5ec6782ed9826453db046001604051808215151515815260200191505060405180910390a1600191506103bd565b80806001019150506102f4565b7f2b766bfa48dbb99822ac647fffc163dc74b7857beedb5ec6782ed9826453db046000604051808215151515815260200191505060405180910390a1600091505b50919050565b60016020528060005260406000206000915054906101000a900460ff1681565b60025481565b6000818154811015156103f857fe5b906000526020600020016000915090505481565b610415816102ec565b1515610489576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f63616e64696461746520697320696e76616c696400000000000000000000000081525060200191505060405180910390fd5b6001806000836000191660001916815260200190815260200160002060008282829054906101000a900460ff160192506101000a81548160ff021916908360ff16021790555060016003600082825401925050819055507f690156027e055e69a001816111c1abd4287fa897e929662c9ad6108a84fe252381336040518083600019166000191681526020018281526020019250505060405180910390a1505600a165627a7a72305820236da75a9249ba5226ada20c64c40e4a7648fe4a78ea37d8fb3c4ca8cfa17a4f0029";
    private static byte[] contractCode = ByteUtils.hexStringToBytes(contractCodeString);
    /**
     * contract id
     */
    private static String testContractId = "CreditManager" + System.currentTimeMillis();

    /**
     * baas上创建的帐户名字
     */
    private static final String account = "sy_test";
    private static Identity userIdentity;
    private static Keypair userKeypair;

    /**
     * create account test
     */
    private static Identity testAccount1 = Utils.getIdentityByName("test_account_"+System.currentTimeMillis());
    /**
     * sdk client
     */
    private static MychainClient sdk;
    /**
     * user password
     */
    private static String userPassword = "12345678"; //根据实际情况更新。申请证书时，创建账户的密码
    /**
     * host ip
     */

    private static String host = "47.103.111.18"; //根据实际情况更新，在BaaS平台，通过查看目标合约链"详情"，在"区块浏览器"中查看"节点详情"可获取链节点的 IP地址 和 端口号。

    /**
     * server port
     */
    private static int port = 18130;               //根据实际情况更新
    /**
     * trustCa password.
     */
    private static String trustStorePassword = "mychain";
    /**
     * mychain environment
     */
    private static ClientEnv env;
    /**
     * mychain is tee Chain
     */
    private static boolean isTeeChain = false;
    /**
     * tee chain publicKeys
     */
    private static List<byte[]> publicKeys = new ArrayList<byte[]>();
    /**
     * tee chain secretKey
     */
    private static String secretKey = "123456";


    private static void exit(String tag, String msg) {
        exit(String.format("%s error : %s ", tag, msg));
    }

    private static void exit(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    private static String getErrorMsg(int errorCode) {
        int minMychainSdkErrorCode = ErrorCode.SDK_INTERNAL_ERROR.getErrorCode();
        if (errorCode < minMychainSdkErrorCode) {
            return ErrorCode.valueOf(errorCode).getErrorDesc();
        } else {
            return ErrorCode.valueOf(errorCode).getErrorDesc();
        }
    }

    private static void initMychainEnv() throws IOException {
        // any user key for sign message
        String userPrivateKeyFile = "user.key";
        userIdentity = Utils.getIdentityByName(account); //根据实际情况更新'gushui03'为'user.key'对应的账户名(BaaS申请证书时创建的账户名)
        Pkcs8KeyOperator pkcs8KeyOperator = new Pkcs8KeyOperator();
        userKeypair = pkcs8KeyOperator.load(IOUtil.inputStreamToByte(DemoSample.class.getClassLoader().getResourceAsStream(userPrivateKeyFile)), userPassword);

        // use publicKeys by tee
        if(isTeeChain) {
            Keypair keypair = new Pkcs8KeyOperator()
                .loadPubkey(
                    IOUtil.inputStreamToByte(DemoSample.class.getClassLoader().getResourceAsStream("test_seal_pubkey.pem")));
            byte[] publicKeyDer = keypair.getPubkeyEncoded(); //tee_rsa_public_key.pem 从BaaS下载获取
            publicKeys.add(publicKeyDer);
        }

        env = buildMychainEnv();

        ILogger logger = AbstractLoggerFactory.getInstance(DemoSample.class);
        env.setLogger(logger);
    }

    private static ClientEnv buildMychainEnv() throws IOException {
        InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(host, port);
        String keyFilePath = "client.key";
        String certFilePath = "client.crt";
        String trustStoreFilePath = "trustCa";

        // build ssl option
        /**
         * client key password
         */
        //根据实际情况更新，申请证书时候指定的SSL密码
        String keyPassword = "Arb1585Stat";
        ISslOption sslOption = new SslBytesOption.Builder()
            .keyBytes(IOUtil.inputStreamToByte(DemoSample.class.getClassLoader().getResourceAsStream(keyFilePath)))
            .certBytes(IOUtil.inputStreamToByte(DemoSample.class.getClassLoader().getResourceAsStream(certFilePath)))
            .keyPassword(keyPassword)
            .trustStorePassword(trustStorePassword)
            .trustStoreBytes(
                IOUtil.inputStreamToByte(DemoSample.class.getClassLoader().getResourceAsStream(trustStoreFilePath)))
            .build();

        List<InetSocketAddress> socketAddressArrayList = new ArrayList<InetSocketAddress>();
        socketAddressArrayList.add(inetSocketAddress);

        List<SignerBase> signerBaseList = new ArrayList<SignerBase>();
        SignerBase signerBase = MyCrypto.getInstance().createSigner(userKeypair);
        signerBaseList.add(signerBase);
        SignerOption signerOption = new SignerOption();
        signerOption.setSigners(signerBaseList);

        return ClientEnv.build(socketAddressArrayList, sslOption, signerOption);
    }


    private static void signRequest(AbstractTransactionRequest request) {
        // sign request
        long ts = sdk.getNetwork().getSystemTimestamp();
        request.setTxTimeNonce(ts, BaseFixedSizeUnsignedInteger.Fixed64BitUnsignedInteger
            .valueOf(RandomUtil.randomize(ts + request.getTransaction().hashCode())), true);
        request.complete();
        sdk.getConfidentialService().signRequest(env.getSignerOption().getSigners(), request);
    }

    private static void deployContract() {
        EVMParameter contractParameters = new EVMParameter();

        // build DeployContractRequest
        DeployContractRequest request = new DeployContractRequest(userIdentity,
            Utils.getIdentityByName(testContractId), contractCode, VMTypeEnum.EVM,
            contractParameters, BigInteger.ZERO);

        TransactionReceiptResponse deployContractResult;
        if (isTeeChain) {
            signRequest(request);

            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);

            deployContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            deployContractResult = sdk.getContractService().deployContract(request);
        }

        // deploy contract
        if (!deployContractResult.isSuccess()
            || deployContractResult.getTransactionReceipt().getResult() != 0) {
            exit("deployContract",
                getErrorMsg((int)deployContractResult.getTransactionReceipt().getResult()));
        } else {
            System.out.println("deploy contract success.");
        }
    }

    private static void issue() {
        EVMParameter parameters = new EVMParameter("Issue(identity,int256)");
        parameters.addIdentity(userIdentity);
        parameters.addUint(BigInteger.valueOf(100));

        // build CallContractRequest
        CallContractRequest request = new CallContractRequest(userIdentity,
            Utils.getIdentityByName(testContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        if (isTeeChain) {
            signRequest(request);

            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);

            callContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            callContractResult = sdk.getContractService().callContract(request);
        }

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            exit("issue", getErrorMsg((int)callContractResult.getTransactionReceipt().getResult()));
        } else {
            System.out.println("issue success.");
        }
    }

    private static void transfer() {
        // contract parameters
        EVMParameter contractParameters = new EVMParameter("Transfer(identity,int256)");
        contractParameters.addIdentity(testAccount1);
        contractParameters.addUint(BigInteger.valueOf(50));

        CallContractRequest request = new CallContractRequest(userIdentity,
            Utils.getIdentityByName(testContractId), contractParameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        if (isTeeChain) {
            signRequest(request);

            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);

            callContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            callContractResult = sdk.getContractService().callContract(request);
        }

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            exit("transfer", getErrorMsg((int)callContractResult.getTransactionReceipt().getResult()));
        } else {
            System.out.println("transfer success.");
        }
    }

    private static BigInteger query(Identity account) {
        // contract parameters
        EVMParameter parameters = new EVMParameter("Query(identity)");
        parameters.addIdentity(account);

        // build call contract request
        CallContractRequest request = new CallContractRequest(userIdentity,
            Utils.getIdentityByName(testContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        if (isTeeChain) {
            signRequest(request);

            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);

            callContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            callContractResult = sdk.getContractService().callContract(request);
        }

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            exit("query", getErrorMsg((int)callContractResult.getTransactionReceipt().getResult()));
        }

        byte[] output = null;
        if (isTeeChain) {
            output = ConfidentialUtil.decrypt(secretKey, callContractResult.getTransactionReceipt().getOutput(), request.getTransaction().getHash().hexStrValue());
        } else {
            output = callContractResult.getTransactionReceipt().getOutput();
        }

        if (output == null) {
            exit("decrypt tee", "decrypt tee output failed");
            return BigInteger.ZERO;
        }

        // decode return values
        EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
        return contractReturnValues.getUint();
    }

    private static void expect(BigInteger balance, BigInteger expectBalance) {
        if (balance.compareTo(expectBalance) != 0) {
            exit("expect", "the account value is not expected.");
        } else {
            System.out.println("check account balance success.");
        }
    }

    private static void initSdk() {
        sdk = new MychainClient();
        boolean initResult = sdk.init(env);
        if (!initResult) {
            exit("initSdk", "sdk init failed.");
        }
    }

    public static void main(String[] args) throws Exception {
        //step 1:init mychain env.
        initMychainEnv();

        //step 2: init sdk client
        initSdk();

        //step 3 : deploy a contract using useridentity.
        deployContract();

        //step 4 issue 100 assets to testAccount1.
        issue();

        //step 5 : transfer 50 assets from useridentity to testAccount1
        transfer();

        //step 6 : query testAccount1 whose balance should be 50.
        BigInteger balance = query(testAccount1);

        //step 7 : compare to expect balance.
        expect(balance, BigInteger.valueOf(50));

        //step 8 : sdk shut down
        sdk.shutDown();
    }
}
