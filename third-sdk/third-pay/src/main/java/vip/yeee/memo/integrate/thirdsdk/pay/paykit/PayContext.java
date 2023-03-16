package vip.yeee.memo.integrate.thirdsdk.pay.paykit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.v3.WxPayV3HttpClientBuilder;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.web.utils.SpringContextUtils;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.AliPayConfig;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxPayConfig;
import vip.yeee.memo.integrate.thirdsdk.pay.service.PayChannelConfigService;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 17:32
 */
@Slf4j
public class PayContext {

    private final String lesseeId;;
    private final PayProperties payProperties;
    private final WxPayConfig wxPayConfig;
    private final AliPayConfig aliPayConfig;
    private final WxPayService wxPayService;
    private final AlipayClient alipayClient;
    private final static ThreadLocal<PayContext> PAY_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
    private final static ConcurrentHashMap<String, PayContext> PAY_CONTEXT_MAP = new ConcurrentHashMap<>();

    public PayContext(String lesseeId, PayProperties payProperties, WxPayConfig wxPayConfig, AliPayConfig aliPayConfig, WxPayService wxPayService, AlipayClient alipayClient) {
        this.lesseeId = lesseeId;
        this.payProperties = payProperties;
        this.wxPayConfig = wxPayConfig;
        this.aliPayConfig = aliPayConfig;
        this.wxPayService = wxPayService;
        this.alipayClient = alipayClient;
    }

    public static void setContext(String lesseeId) {
        try {
            PayContext payContext = PAY_CONTEXT_MAP.get(lesseeId);
            if (payContext == null) {
                PayProperties payProperties = (PayProperties) SpringContextUtils.getBean(PayProperties.class);
                PayChannelConfigService channelConfigService = (PayChannelConfigService) SpringContextUtils.getBean(PayChannelConfigService.class);
                WxPayConfig wxPayChannelConfig = channelConfigService.getWxPayChannelConfig(lesseeId);
                WxPayService wxPayService = null;
                if (wxPayChannelConfig != null) {
                    wxPayService = new WxPayServiceImpl();
                    com.github.binarywang.wxpay.config.WxPayConfig wxPayConfig = new com.github.binarywang.wxpay.config.WxPayConfig();
                    BeanUtils.copyProperties(wxPayChannelConfig, wxPayConfig);
                    if (PayConstant.PAY_IF_VERSION.WX_V2.equals(wxPayChannelConfig.getApiVersion())) {
                        wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
                    }
                    wxPayConfig.setPrivateKeyContent(FileUtil.readBytes(wxPayChannelConfig.getPrivateKeyPath()));
                    byte[] wxPayCertInfo = getWxPayCertInfo(wxPayChannelConfig.getMchId(), wxPayChannelConfig.getCertSerialNo(), wxPayChannelConfig.getApiV3Key(), wxPayConfig.getPrivateKeyContent());
                    wxPayConfig.setPrivateCertContent(wxPayCertInfo);
                    wxPayService.setConfig(wxPayConfig);
                }
                AliPayConfig aliPayChannelConfig = channelConfigService.getAliPayChannelConfig(lesseeId);
                AlipayClient alipayClient = null;
                if (aliPayChannelConfig != null) {
                    CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                    certAlipayRequest.setServerUrl(aliPayChannelConfig.getGatewayUrl());
                    certAlipayRequest.setAppId(aliPayChannelConfig.getAppId());
                    certAlipayRequest.setFormat(AlipayConstants.FORMAT_JSON);
                    certAlipayRequest.setPrivateKey(getAliPayPrivateKey(aliPayChannelConfig.getPrivateKey()));
                    certAlipayRequest.setCharset(AlipayConstants.CHARSET_UTF8);
                    certAlipayRequest.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
                    certAlipayRequest.setCertPath(aliPayChannelConfig.getAppPublicCert());
                    certAlipayRequest.setAlipayPublicCertPath(aliPayChannelConfig.getAlipayPublicCert());
                    certAlipayRequest.setRootCertPath(aliPayChannelConfig.getAlipayRootCert());
                    alipayClient = new DefaultAlipayClient(certAlipayRequest);
                }
                payContext = new PayContext(lesseeId, payProperties, wxPayChannelConfig, aliPayChannelConfig, wxPayService, alipayClient);
                PAY_CONTEXT_MAP.put(lesseeId, payContext);
            }
            PAY_CONTEXT_THREAD_LOCAL.set(payContext);
        } catch (Exception e) {
            log.error("初始化支付上下文失败", e);
            throw new BizException("初始化支付上下文失败");
        }
    }

    public static PayContext getContext() {
        return PAY_CONTEXT_THREAD_LOCAL.get();
    }

    public static void clearContext() {
        PAY_CONTEXT_THREAD_LOCAL.remove();
    }

    public static PayKit getPayChannelKit(String payWay) {
        PayContext context = PayContext.getContext();
        String beanName = null;
        if (payWay.startsWith(PayConstant.IF_CODE.WXPAY)) {
            WxPayConfig wxPayConfig = context.getWxPayConfig();
            if (wxPayConfig == null) {
                throw new BizException("未开通微信商户");
            }
            if (Integer.valueOf(1).equals(wxPayConfig.getPartner())) {
                beanName = "PARTNER_" + payWay;
            }
            if (PayConstant.PAY_IF_VERSION.WX_V3.equals(context.getWxPayConfig().getApiVersion())) {
                beanName = beanName + StrUtil.toCamelCase(payWay) + "V3PayKit";
            } else {
                beanName = beanName + StrUtil.toCamelCase(payWay) + "PayKit";
            }
        } else if (payWay.startsWith(PayConstant.IF_CODE.ALIPAY)) {
            if (context.getAliPayConfig() == null) {
                throw new BizException("未开通支付宝商户");
            }
            beanName = StrUtil.toCamelCase(payWay) + "PayKit";
        }
        try {
            return (PayKit) SpringContextUtils.getBean(beanName);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BizException("不支持的支付方式");
        }
    }

    public String getLesseeId() {
        return lesseeId;
    }

    public PayProperties getPayProperties() {
        return payProperties;
    }

    public WxPayConfig getWxPayConfig() {
        return wxPayConfig;
    }

    public AliPayConfig getAliPayConfig() {
        return aliPayConfig;
    }

    public WxPayService getWxPayService() {
        return wxPayService;
    }

    public AlipayClient getAlipayClient() {
        return alipayClient;
    }

    private static byte[] getWxPayCertInfo(String mchId, String certSerialNo, String apiV3Key, byte[] privateCertContent) {
        try {
            CloseableHttpClient client = WxPayV3HttpClientBuilder.create()
                    .withMerchant(mchId, certSerialNo, PemUtils.loadPrivateKey(new ByteArrayInputStream(privateCertContent)))
                    .withValidator(response -> true)
                    .build();
            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/certificates");
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");
            CloseableHttpResponse response = client.execute(httpGet);
            String bodyAsString = EntityUtils.toString(response.getEntity());
            JSONObject obj = JSONObject.parseObject(bodyAsString);
            if(obj.containsKey("code")){
                log.error("加载平台证书失败:{}", obj.getString("message"));
            }
            JSONObject data = obj.getJSONArray("data").getJSONObject(0);
            String expire_time = data.getString("expire_time");
            JSONObject encryptCertificate = data.getJSONObject("encrypt_certificate");
            if(DateUtil.parse(expire_time, "yyyy-MM-dd'T'HH:mm:ssXXX").before(new Date())){
                log.error("加载平台证书失败:证书已过期");
            }
            String certificateStr = decryptToString(apiV3Key, encryptCertificate.getString("associated_data"),
                    encryptCertificate.getString("nonce"), encryptCertificate.getString("ciphertext"));
            return certificateStr.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.info("getWxPayCertInfo error", e);
        }
        return null;
    }

    private static String decryptToString(String apiV3Key, String associatedData, String nonce, String ciphertext)
            throws GeneralSecurityException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(apiV3Key.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String getAliPayPrivateKey(String privateKey) {
        if (privateKey.startsWith("/")) {
            return FileUtil.readString(privateKey, StandardCharsets.UTF_8);
        }
        return privateKey;
    }

}
