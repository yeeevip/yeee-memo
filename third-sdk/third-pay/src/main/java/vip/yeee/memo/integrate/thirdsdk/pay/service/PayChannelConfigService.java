package vip.yeee.memo.integrate.thirdsdk.pay.service;

import org.springframework.stereotype.Service;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.AliPayConfig;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxPayConfig;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/16 20:28
 */
@Service
public class PayChannelConfigService {

    public WxPayConfig getWxPayChannelConfig(String lesseeId) {
//        WxPayConfig wxPayConfig = new WxPayConfig();
//        wxPayConfig.setApiVersion("V3");
//        wxPayConfig.setAppId(wxPayConfigVo.getAppId());
//        wxPayConfig.setMchId(wxPayConfigVo.getMchId());
//        wxPayConfig.setSubMchId(wxPayConfigVo.getSubMchId());
//        wxPayConfig.setApiV3Key(wxPayConfigVo.getApiV3Key());
//        wxPayConfig.setCertSerialNo(wxPayConfigVo.getMchSerialNo());
//        wxPayConfig.setKeyPath(wxPayConfigVo.getMerchantPrivateKeyPath());
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setApiVersion("V3");
        payConfig.setAppId("wxd930ea5d5a258f4f");
        payConfig.setSubAppId("");
        payConfig.setMchId("10000100");
        payConfig.setSubMchId("");
        payConfig.setMchKey("");
        payConfig.setKeyPath("");
        payConfig.setApiV3Key("192006250b4c09247ec02edce69f6a2d");
        payConfig.setCertSerialNo("");
        payConfig.setPrivateCertPath("classpath:path");
        payConfig.setPrivateKeyPath("classpath:path");
        return payConfig;
    }

    public AliPayConfig getAliPayChannelConfig(String lesseeId) {
//        AliPayConfig aliPayConfig = new AliPayConfig();
//        aliPayConfig.setAppId(aliPayConfigVo.getAppId());
//        aliPayConfig.setMchAppId(aliPayConfigVo.getMchAppId());
//        aliPayConfig.setSignType("RSA2");
//        aliPayConfig.setUseCert(true);
//        aliPayConfig.setPrivateKey("");
//        aliPayConfig.setAppPublicCert(aliPayConfigVo.getMerchantCertPath());
//        aliPayConfig.setAlipayPublicCert(aliPayConfigVo.getAlipayCertPath());
//        aliPayConfig.setAlipayRootCert(aliPayConfigVo.getAlipayRootCertPath());
//        aliPayConfig.setAuthToken(aliPayConfigVo.getAuthToken());
        AliPayConfig payConfig = new AliPayConfig();
        payConfig.setAppId("2016XXXXXXXXXXXXX");
        payConfig.setMchAppId("");
        payConfig.setUseCert(false);
        payConfig.setPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCTHcZRMkV1ZfgqUhIhmFBHcLh5VFOWZV8GknetCtJFuIZoCI0saE7mrMfIZS3RKjzKoBCeDrBsg5Jjfl2xiOCibRiwfMxXZ8PEDPLnsvhDmhR0g/lG3+BaVhxz49w2uhzT0Acj/tFPClIFsYhJD5CsfAnAlT5ZgFcxKsUl8dYwBFWECN+YpPg0QWg0cWZ1noCwLW/A1AxV1pGEd8oYEL0AZirWMT8iaM94yakqy4mMk8DeOcSPgWVF0o/WHi05vGFLGVG90DMf8YpBelOeDYKzLizmN899KmyJuL0m+tvzgvv9qmpVNb5IpLrsTSDZk5ErVmkOLZJ4MoqGbxIZl7xZAgMBAAECggEACzeY+HXxz9q/ilKJqWAYtunEVxPz76K5FSIz19DcaY6BSQtl4D3vzizcas40KR+kVPrRaYvLLer2TXnEIRZDsn7JBCPv3LY/ugIWv03pGhZ0etHZPusxCxJHxxpXnyCftBLHiJoUUYeC64Y20wNJz60BfVqjz2U2wLv3HkaLQ0vjrHOnHfZoWPLVK5YYaqqC2Yg/aqVv4Cj7N3YdcM5PP59LiNL75n/fIBiLCYjGjX6zwmDtV7gy7VFI99/Hi7ip+rqacTxFWp4WCQ1a5cdagv4eWohgnHuMSWn/K9NhZdb13LNRFGVHAiuav3yWMfaokL1j9o8kxLqXaUQVFGmSdQKBgQDLmddy4i8d3iNgskzSCuA+CZYxCKIXFBdgjmJcYvQyd0ZtW/v8dSVSxcz4/1ygv8z4Z5i/0p5EBhXbpFK70ERuBUCMpeVVugjjVB3cgN1rRson14xjEQAdfbGLZ8mgVoRq3PwjyHBU401bAg4MUpYHQYRyc9osBb0RIpZzdtN9IwKBgQC4+ne8PhesxiZSl1a8B9ICTX7cnjqJvoBCtzTjpQmvefuqadbA/K7VsEnv8q3eFC3Ype5XCFKGC2tv8m9pPZqzNtuHD68Zf1kR8O1mTbDEqwN7ub4qpkC1W1GaRELyfudSzBbzhQRahBl9+5iy9K7SOxMKovpyY/EwcdutAZzOUwKBgQDBp0csp4RHNWWS8KRu2BdoCnhHlT2PE/YUSfm2hAxe/+IF3Ir2Gnpwm2EVqNXys9wCnY7FTVBpxRv/OPHUYAjmWKqH3Lcgbf1MNFLey9dO1RstADEwfRgN9OLthL9beU8j0aMlad2mcDjAiljQUbEPn7qZniXy31ZwuOQ/WNNZ3wKBgQCFgpJFVfO0ilhCqGS71lEnxFNaXQIXRV5ByQXtgMMa2kpg139fT4HJTjIvc9M2RQ5KHomGW8VkZn2nES6EoSg1TdTpCNLy7k2Ve3V9r3l0mZIsDQZKtjBiNYUSeU5wxFOmGRQ/s7ROuKXFnoC6OYysxGKb/MITKjt9IdDScCWDbQKBgBfjukJmFpOduV5njMI1H8tM3S9Pzlwzi7S9swi0b6zttDXxCTv+cls0ZTp2QH/DfqJX8UR76el+vY+XxiEM1Ycnu/zsK71VJtgDWTU7Dz+ush7XBoObQ+dspFDqaYI72SDLqLEWkBp+OhrPwPOTejnMj1mizsLPDGXHNFvIw85Y");
        payConfig.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAixptl13fqQpcRJSK2bYPaeHexP5ti0hfKrJ1iGx4zEv3XcxCp7FBA+VQerG+XxbvqNwAge7KBHSSDKbwAivzAPaON9A0Fuq6860PLX3FjcN2ELfuO3oxN8lGr38p59vRD9O2dNQa5lzQS6trcykBd5BM634h2P/1fTbWrTSy/1dJ3f1ChJzndIOgfcXWaQHnW0yGJpLDBy8HvHb862HVbXxSHN+LSU9VDeD2eTf79R/p8kC6qJlrA2iINyyc3zfUjIWtpHT8o93YNJqVABgweucy3PTIbSkMKOQu93X0kyxv9fuBCoeX7QxZQaTJjUa7E9CjJwzuJfsL3KkgEc6PCQIDAQAB");
        payConfig.setSignType("RSA2");
        payConfig.setAppPublicCert("");
        payConfig.setAlipayPublicCert("");
        payConfig.setAlipayRootCert("");
        payConfig.setAuthToken("");
        return payConfig;
    }
}
