package vip.yeee.memo.demo.blockchain.hyperchain.biz.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BlockchainAccountBO {
    private String address;
    private String publicKey;
    private String privateKey;
    private String version;
    private String algo;
}
