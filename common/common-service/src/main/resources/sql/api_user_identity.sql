create table `api_user_identity` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_type` tinyint(4) DEFAULT NULL COMMENT '10：微信-公众号，20：微信-小程序，30：百度-小程序，。。。',
    `app_type` tinyint(4) DEFAULT NULL COMMENT '10：我要授权，20：在线访谈，。。。',
    `subject_id` int(11) DEFAULT NULL COMMENT '实体ID',
    `union_id` varchar(64) DEFAULT NULL COMMENT '',
    `open_id` varchar(64) DEFAULT NULL COMMENT '',
    `mobile` varchar(64) DEFAULT NULL COMMENT '手机号',
    `qq` varchar(64) DEFAULT NULL COMMENT 'qq号',
    `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
    `status` tinyint(2) DEFAULT NULL COMMENT '',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
    `del` tinyint(1) DEFAULT '0' COMMENT '0-有效  1-删除',
    PRIMARY KEY (`id`),
    KEY `idx_subjectId` (`subject_id`) USING BTREE,
    KEY `idx_userType_appType_openId_mobile` (`user_type`,`app_type`,`open_id`,`mobile`,`subject_id`) USING BTREE,
    KEY `idx_userType_appType_mobile_openId` (`user_type`,`app_type`,`mobile`,`open_id`,`subject_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COMMENT='三方开放平台用户';