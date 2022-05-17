package com.yeeee.chain.biz.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/17 9:43
 */
@Data
@Accessors(chain = true)
public class BlockchainAccountBO {
    private String address;
    private String publicKey;
    private String privateKey;
    private String version;
    private String algo;
}
