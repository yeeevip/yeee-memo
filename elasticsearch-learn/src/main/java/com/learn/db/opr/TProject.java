package com.learn.db.opr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 众筹项目表
 * </p>
 *
 * @author yeeee
 * @since 2022-05-17
 */
@Data
@Accessors(chain = true)
@TableName("t_project")
public class TProject {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String blurb;

    /**
     * 封面图片路径
     */
    private String coverPath;

    /**
     * 目标集资
     */
    private Integer totalFundRaising;

    /**
     * 已经集资
     */
    private Integer hasFundRaising;

    /**
     * 发起人ID
     */
    private Integer userId;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 发起时间
     */
    private LocalDateTime launchDateRaising;

    /**
     * 集资天数
     */
    private Integer daysRaising;

    /**
     * 项目状态 0：进行中  -1：集资失败 1：成功
     */
    private Integer hasFinish;

    /**
     * -1:审核不通过 0:未审核 1:审核通过
     */
    private Integer hasAudits;

    /**
     * 是否首页展示
     */
    private Integer hasIndex;

    /**
     * 发起身份
     */
    private String shenfen;

    /**
     * 上线时间，审核通过后即上线
     */
    private LocalDateTime onlineTime;

    /**
     * 结算状态    -1：不可结算 0：未结算  1：已结算
     */
    private Integer isSettlement;


}
