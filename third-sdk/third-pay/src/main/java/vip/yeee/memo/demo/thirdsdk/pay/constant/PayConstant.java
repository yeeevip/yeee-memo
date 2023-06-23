package vip.yeee.memo.demo.thirdsdk.pay.constant;

public class PayConstant {

    //接口类型
    public interface IF_CODE {
        String ALIPAY = "ALI";   // 支付宝官方支付
        String WXPAY = "WX";     // 微信官方支付
    }


    //支付方式代码
    public interface PAY_WAY_CODE {

        String ALI_APP = "ALI_APP";  //支付宝 app支付
        String ALI_PC = "ALI_PC";  //支付宝 电脑网站支付
        String ALI_WAP = "ALI_WAP";  //支付宝 wap支付
        String ALI_BAR = "ALI_BAR";  //支付宝 商家扫码枪主动扫描用户付款码
        String ALI_QR = "ALI_QR";  //支付宝 用户扫描二维码付款

        String WX_JSAPI = "WX_JSAPI";  //微信jsapi支付
        String WX_WP = "WX_WP";  //微信jsapi支付
        String WX_MINI = "WX_MINI";  //微信jsapi支付
        String WX_H5 = "WX_H5";  //微信H5支付
        String WX_APP = "WX_APP";  //微信APP支付
        String WX_BAR = "WX_BAR";  //微信扫码枪
        String WX_NATIVE = "WX_NATIVE";  //微信原生扫码支付
    }

    //支付数据包 类型
    public interface PAY_DATA_TYPE {
        String PAY_URL = "payurl";  //跳转链接的方式  redirectUrl
        String FORM = "form";  //表单提交
        String WX_APP = "wxapp";  //微信app参数
        String ALI_APP = "aliapp";  //支付宝app参数
        String YSF_APP = "ysfapp";  //云闪付app参数
        String CODE_URL = "codeUrl";  //二维码URL
        String CODE_IMG_URL = "codeImgUrl";  //二维码图片显示URL
        String NONE = "none";  //无参数
//        String QR_CONTENT = "qrContent";  //二维码实际内容
    }


    //接口版本
    public interface PAY_IF_VERSION {
        String WX_V2 = "V2";  //微信接口版本V2
        String WX_V3 = "V3";  //微信接口版本V3
    }
}
