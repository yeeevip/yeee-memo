package com.yeeee.chain.biz.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NftIssueBO {
    private String tokenId;
    private BlockchainAccountBO accountBO;
}
