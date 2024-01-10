package vip.yeee.memo.demo.thirdsdk.aliyun.pnp;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxbRequest;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxbResponse;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionRequest;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.redis.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/12/27 9:57
 */
@Slf4j
@Component
public class PhoneProtectService {

    private final long expireTime = TimeUnit.DAYS.toMillis(7);
    private final String product = "Dyplsapi";
    private final String domain = "dyplsapi.aliyuncs.com";
    @Value("${aliyun.pnp.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.pnp.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.pnp.poolKey}")
    private String poolKey;
    @Resource
    private RedisUtil redisUtil;

    public String getProtectedPhone(String phoneA, String phoneB) {
        try {
            String cacheKeyPre = "AGENCY-API:PNP_AXB:" + poolKey + ":";
            String sortedABStr = Stream.of(phoneA, phoneB).sorted().collect(Collectors.joining("_"));
            String phoneABXKey = cacheKeyPre + "VALUE:" + sortedABStr;
            String phoneAXKey = cacheKeyPre + "LIST:" + phoneA;
            String phoneBXKey = cacheKeyPre + "LIST:" + phoneB;
            String value = redisUtil.getValue(phoneABXKey);
            if (value != null) {
                return value;
            }

            long exp = System.currentTimeMillis() + expireTime;
            BindAxbResponse axbResponse = this.bindAxb(phoneA, phoneB, exp);
            if ("isv.NO_AVAILABLE_NUMBER".equals(axbResponse.getCode())) {
                String[] tempArr = new String[] {""};
                boolean flag = this.unbindPhone(phoneA, tempArr) || this.unbindPhone(phoneB, tempArr);
                if (flag) {
                    axbResponse = this.bindAxb(phoneA, phoneB, exp);
                }
            }
            if (!"OK".equals(axbResponse.getCode())) {
                throw new BizException(axbResponse.getMessage());
            }

            String axbSubId = axbResponse.getSecretBindDTO().getSubsId();
            String axbSecretNo = axbResponse.getSecretBindDTO().getSecretNo();
            redisUtil.cacheValue(phoneABXKey, axbSecretNo, expireTime / 1000 - 1);
            redisUtil.cacheList(phoneAXKey, sortedABStr + "##" + axbSubId + "##" + axbSecretNo);
            redisUtil.cacheList(phoneBXKey, sortedABStr + "##" + axbSubId + "##" + axbSecretNo);

            String limitedTimeKey = cacheKeyPre + "ZSET:" + axbSecretNo;
            redisUtil.removeZSetRangeByScore(limitedTimeKey, 0, System.currentTimeMillis());
            Long size = redisUtil.getZSetSize(limitedTimeKey);
            if (size != null && size >= 196) {
                Set<String> s = redisUtil.getZSet(limitedTimeKey, size - 1, size - 1);
                String m = s.toArray(new String[1])[0];
                String[] sArr = m.split("##");
                this.unbind(sArr[1], axbSecretNo);
                redisUtil.removeZSetValue(limitedTimeKey, m);
                redisUtil.remove(cacheKeyPre + "VALUE:" + sArr[0]);
            }
            redisUtil.cacheZSet(limitedTimeKey, sortedABStr + "##" + axbSubId, exp);

            return axbSecretNo;
        } catch (Exception e) {
            if (e instanceof BizException) {
                throw (BizException)e;
            }
            log.error("getProtectedPhone error，phoneA = {}，phoneB = {}", phoneA, phoneB, e);
            throw new BizException(StrUtil.format("获取隐私号失败, {}", e.getMessage()));
        }
    }

    private boolean unbindPhone(String phoneA, String[] tempArr) throws ClientException {
        String cacheKeyPre = "AGENCY-API:PNP_AXB:" + poolKey + ":";
        String value = redisUtil.popOneOfList(cacheKeyPre + "LIST:" + phoneA);
        if (StrUtil.isNotBlank(value) && !value.equals(tempArr[0])) {
            tempArr[0] = value;
            String[] xArr = value.split("##");
            this.unbind(xArr[1], xArr[2]);
            String phoneBXKey = cacheKeyPre + "LIST:" + xArr[0].replaceAll(phoneA, "").replaceAll("_", "");
            redisUtil.removeList(phoneBXKey, 1, value);
            redisUtil.removeZSetValue(cacheKeyPre + "ZSET:" + xArr[2], xArr[0] + "##" + xArr[1]);
            redisUtil.remove(cacheKeyPre + "VALUE:" + xArr[0]);
            return true;
        }
        return false;
    }

    /**
     * AXB绑定示例
     *
     * @return
     * @throws ClientException
     */
    private BindAxbResponse bindAxb(String phoneA, String phoneB, long exp) throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient,暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //AXB绑定请求结构体-参数说明详见参数说明
        BindAxbRequest request = new BindAxbRequest();
        //必填:号池Key-详见概览页面的号池管理功能
        request.setPoolKey(poolKey);
        //必填:AXB关系中的A号码
        request.setPhoneNoA(phoneA);
        //必选:AXB中的B号码
        request.setPhoneNoB(phoneB);
        //可选:指定X号码选号
//        request.setPhoneNoX("XXXXXXXXXXXXXX");
        //可选:指定需要分配归属城市的X号码
        //request.setExpectCity("北京');
        //必填:绑定关系对应的失效时间-不能早于当前系统时间(100秒)
        request.setExpiration(DateUtil.format(new Date(exp), DatePattern.NORM_DATETIME_PATTERN));
        //可选:是否需要录制音频-默认是false
        request.setIsRecordingEnabled(false);
        //hint 此处可能会抛出异常，注意catch
        BindAxbResponse response = acsClient.getAcsResponse(request);
        log.info("bindAxb response = {}", JSONObject.toJSONString(response));
        return response;
    }

    /**
     * 订购关系解绑示例(解绑接口不区分AXB、AXN)
     *
     * @return
     * @throws ClientException
     */
    private UnbindSubscriptionResponse unbind(String subsId, String secretNo) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        UnbindSubscriptionRequest request = new UnbindSubscriptionRequest();
        //必填:对应的号池Key
        request.setPoolKey(poolKey);
        //必填-分配的X小号-对应到绑定接口中返回的secretNo;
        request.setSecretNo(secretNo);
        //可选-绑定关系对应的ID-对应到绑定接口中返回的subsId;
        request.setSubsId(subsId);
        UnbindSubscriptionResponse response = acsClient.getAcsResponse(request);
        log.info("unbind response = {}", JSONObject.toJSONString(response));
        return response;
    }
}
