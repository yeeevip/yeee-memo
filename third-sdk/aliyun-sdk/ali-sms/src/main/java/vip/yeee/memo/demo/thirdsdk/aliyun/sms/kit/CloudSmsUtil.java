package vip.yeee.memo.demo.thirdsdk.aliyun.sms.kit;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.HashMap;
import java.util.Map;

public class CloudSmsUtil {

    private final static String accessKeyId = "";

    private final static String accessKeySecret = "";

    private final static String showStartNoticeTemplate = "";

    private final static String signName = "";

    /**ali固定配置参数begin*/
    //短信API产品名称
    static final String product="Dysmsapi";
    //短信API产品域名
    static final String domain="dysmsapi.aliyuncs.com";
    /**ali固定配置参数end*/

    public final static Map<String, String> PHONE_CODE_MAP = new HashMap<>();

    public static boolean sendCheckCode(String phone, String code) {
        JSONObject params = new JSONObject();
        params.put("code", code);
        return  aliSmsSendMsg(showStartNoticeTemplate, phone, params);
    }


    private static boolean aliSmsSendMsg(String templateCode, String phone, JSONObject params) {

//    System.out.println("alibaba-phone:" + phone + "-code:" + code);
        System.out.println("配置信息");
        System.out.println("accessKeyId:" + accessKeyId);
        System.out.println("accessKeySecret:" + accessKeySecret);
        System.out.println("signName:" + signName);
        System.out.println("templateCode:" + templateCode);
        try {
            //设置超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化ascClient
            IClientProfile profile= DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient=new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request=new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //待发送的手机号
            request.setPhoneNumbers(phone);
            //短信签名
            request.setSignName(signName);
            //短信模板ID
            request.setTemplateCode(templateCode);
            /*
             * 可选:模板中的变量替换JSON串,
             * 如模板内容为"亲爱的${name},您的验证码为${code}"时,
             * 此处的值为{"name":"Tom","code":"1454"}
             *   \  反斜杠为转义字符，使得输出双引号
             */
            request.setTemplateParam(params.toString());
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //request.setOutId("1454");
            SendSmsResponse response=acsClient.getAcsResponse(request);

            System.out.println(response.getCode() + ":" + response.getMessage());
            if(response.getCode() != null && response.getCode().equals("OK")) {
                //请求成功
                System.out.println("发送成功！");
                return true;
            }else {
                System.out.println(StrUtil.format("发送失败！{}", response.getCode() + ":" + response.getMessage()));
                return false;
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
