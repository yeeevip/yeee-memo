
CREATE TABLE `t_pay_order` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
       `lessee_id` varchar(32) NOT NULL,
       `code` varchar(32) NOT NULL COMMENT '订单号',
       `out_code` varchar(100) DEFAULT NULL COMMENT '渠道订单号',
       `mch_id` VARCHAR(64) NOT NULL COMMENT '商户号',
       `state` tinyint(6) NOT NULL DEFAULT '0' COMMENT '支付状态: 0-订单生成, 10-支付中, 20-支付成功, 30-支付失败, 40-已撤销（退款中）, 50-已退款, 60-订单关闭',
       `amount` decimal(11,2) NOT NULL COMMENT '支付金额',
       `currency` varchar(3) NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
       `user_id` varchar(64) NOT NULL COMMENT '下单用户ID',
       `channel` varchar(16) DEFAULT NULL COMMENT '支付渠道，wx、ali',
       `channel_user` varchar(64) DEFAULT NULL COMMENT '渠道用户标识,如微信openId,支付宝账号',
       `payway` varchar(16) DEFAULT NULL COMMENT '支付方式，WX_APP、ALI_APP',
       `refund_state` tinyint(6) NOT NULL DEFAULT '0' COMMENT '退款状态: 0-未发生实际退款, 10-部分退款, 20-全额退款',
       `refund_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '退款总金额,单位分',
       `expired_time` datetime DEFAULT NULL COMMENT '订单失效时间',
       `success_time` datetime DEFAULT NULL COMMENT '订单支付成功时间',
       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       `deleted` tinyint(1) DEFAULT '0',
       PRIMARY KEY (`id`),
       KEY `created_time` (`create_time`),
       KEY `order_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE `cloud_entry_order_refund` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `lessee_id` varchar(32) NOT NULL,
    `code` varchar(32) NOT NULL,
    `out_code` varchar(100) NOT NULL,
    `mch_id` varchar(32) DEFAULT NULL,
    `channel` varchar(3) NOT NULL COMMENT '支付渠道，wx、ali',
    `order_code` varchar(32) NOT NULL,
    `amount` decimal(11,2) NOT NULL,
    `currency` varchar(3) NOT NULL DEFAULT 'cny',
    `state` tinyint(3) NOT NULL DEFAULT '0' COMMENT '退款状态:0-订单生成,10-退款中,20-退款成功,30-退款失败,40-退款任务关闭',
    `create_time` datetime DEFAULT NULL,
    `success_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    `deleted` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;