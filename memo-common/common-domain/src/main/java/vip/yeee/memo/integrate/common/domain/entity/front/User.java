package vip.yeee.memo.integrate.common.domain.entity.front;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@TableName("t_user")
@Data
@Accessors(chain = true)
public class User {
    /**
     * 用户id主键
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别（1：男，2：女）
     */
    private Integer sex;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 出生日期
     */
    private Date dateOfBirth;

    /**
     * 注册时间
     */
    private Date dateOfRegistration;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 城市
     */
    private String city;

    /**
     * 头像路径
     */
    private String imgPath;

    /**
     * 电子邮箱
     */
    private String email;
}