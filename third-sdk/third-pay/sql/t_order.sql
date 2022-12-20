
CREATE TABLE `t_order` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
       `code` varchar(32) NOT NULL COMMENT '订单号',
       `amount` bigint(20) NOT NULL COMMENT '支付金额,单位分',
       `currency` varchar(3) NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
       `state` tinyint(6) NOT NULL DEFAULT '0' COMMENT '支付状态: 0-订单生成, 10-支付中, 20-支付成功, 30-支付失败, 40-已撤销, 50-已退款, 60-订单关闭',
       `subject_id` bigint(64) NOT NULL COMMENT '商品ID',
       `user_id` varchar(64) NOT NULL COMMENT '下单用户ID',
       `channel_user` varchar(64) DEFAULT NULL COMMENT '渠道用户标识,如微信openId,支付宝账号',
       `channel_order_no` varchar(64) DEFAULT NULL COMMENT '渠道订单号',
       `refund_state` tinyint(6) NOT NULL DEFAULT '0' COMMENT '退款状态: 0-未发生实际退款, 10-部分退款, 20-全额退款',
       `refund_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '退款总金额,单位分',
       `channel` varchar(16) DEFAULT NULL COMMENT '支付渠道，wx、ali',
       `payway` varchar(16) DEFAULT NULL COMMENT '支付方式，WX_APP、ALI_APP',
       `expired_time` datetime DEFAULT NULL COMMENT '订单失效时间',
       `success_time` datetime DEFAULT NULL COMMENT '订单支付成功时间',
       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`),
       KEY `created_time` (`create_time`),
       KEY `order_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';