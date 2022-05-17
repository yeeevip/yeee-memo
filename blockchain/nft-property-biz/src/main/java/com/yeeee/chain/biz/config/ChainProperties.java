package com.yeeee.chain.biz.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/5/17 11:15
 */
@Data
//@Component
//@ConfigurationProperties(prefix = "yeeee.chain")
public class ChainProperties {
    /**
     * 默认节点
     */
    private String defaultUrl;
    /**
     * 合约地址
     */
    private String contractAddress;
    /**
     * 部署账号信息
     */
    private String deployAccountJson;

    public void setDeployAccountJson(String deployAccountJson) {
        this.deployAccountJson = StrUtil.removePrefix(deployAccountJson, "string:");
    }
}
