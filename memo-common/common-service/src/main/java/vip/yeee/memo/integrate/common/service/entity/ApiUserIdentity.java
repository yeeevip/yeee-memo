package vip.yeee.memo.integrate.common.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 三方开放平台用户
 * </p>
 *
 * @author yeeeee
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("api_user_identity")
public class ApiUserIdentity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 10：微信-公众号，20：微信-小程序，30：百度-小程序，。。。
     */
    private Integer userType;

    /**
     * 10：我要授权，20：在线访谈，。。。
     */
    private Integer appType;

    /**
     * 实体ID
     */
    private Integer subjectId;

    private String unionId;

    private String openId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * qq号
     */
    private String qq;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 订阅状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 0-有效  1-删除
     */
    @TableLogic
    private Integer del;


}
