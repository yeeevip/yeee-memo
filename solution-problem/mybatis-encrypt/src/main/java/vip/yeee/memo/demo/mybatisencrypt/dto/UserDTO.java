package vip.yeee.memo.demo.mybatisencrypt.dto;

import lombok.Data;
import vip.yeee.memo.common.encrypt.mybatis.annotation.*;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveType;

@SensitiveEncryptEnabled
@Data
public class UserDTO {

    private Integer id;
    /**
     * 用户名
     */
    @EncryptField
    private String userName;
    /**
     * 脱敏的用户名
     */
    @SensitiveField(SensitiveType.CHINESE_NAME)
    private String userNameSensitive;
    /**
     * 值的赋值不从数据库取，而是从userName字段获得。
     */
    @SensitiveBind(bindField = "userName",value = SensitiveType.CHINESE_NAME)
    private String userNameOnlyDTO;
    /**
     * 身份证号
     */
    @EncryptField
    private String idcard;
    /**
     * 脱敏的身份证号
     */
    @SensitiveField(SensitiveType.ID_CARD)
    private String idcardSensitive;
    /**
     * 一个json串，需要脱敏
     * SensitiveJSONField标记json中需要脱敏的字段
     */
    @SensitiveJSONField(fieldKeyList = {
            @SensitiveJSONFieldKey(key = "idcard",type = SensitiveType.ID_CARD),
            @SensitiveJSONFieldKey(key = "username",type = SensitiveType.CHINESE_NAME),
    })
    private String jsonStr;

    private int age;

    @SensitiveField(SensitiveType.EMAIL)
    private String email;
}
