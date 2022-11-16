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

CREATE TABLE `sys_user`  (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
    `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
    `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '花名',
    `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
    `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别',
    `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
    `dept_id` int(11) unsigned DEFAULT NULL COMMENT '部门名称',
    `job_id` int(11) unsigned DEFAULT NULL COMMENT '岗位名称',
    `state` int(4) DEFAULT '0' COMMENT '状态：（0正常 1停用）',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登陆时间',
    `create_time` datetime DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
    `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `super_admin` tinyint(4) DEFAULT NULL COMMENT '超级管理员',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `username_idx` (`username`) USING BTREE,
    KEY `dept_idx` (`dept_id`) USING BTREE,
    KEY `job_idx` (`job_id`) USING BTREE,
    KEY `create_time_idx` (`create_time`) USING BTREE,
    KEY `update_time_idx` (`update_time`) USING BTREE,
    KEY `email_idx` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统用户';

CREATE TABLE `sys_role`  (
    `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色code',
    `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
    `status` int(2) NULL DEFAULT 0 COMMENT '状态 （0正常 1停用）',
    `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
    `create_by` varchar(32) NULL DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(32) NULL DEFAULT NULL COMMENT '更新人',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

CREATE TABLE `sys_user_role`  (
    `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` int(11) UNSIGNED NOT NULL COMMENT '用户ID',
    `role_id` int(11) UNSIGNED NOT NULL COMMENT '角色ID',
    `create_by` varchar(32) NULL DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(32) NULL DEFAULT NULL COMMENT '更新人',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_role_id`(`role_id`) USING BTREE,
    INDEX `idx_user_role`(`role_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 367 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

CREATE TABLE `t_user`  (
    `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户id主键',
    `nick_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
    `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户名',
    `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
    `real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '真实姓名',
    `sex` tinyint(2) NULL DEFAULT NULL COMMENT '性别（1：男，2：女）',
    `id_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '身份证号',
    `date_of_birth` date NULL DEFAULT NULL COMMENT '出生日期',
    `date_of_registration` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
    `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '联系电话',
    `city` varchar(14) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '城市',
    `img_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像路径',
    `email` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '电子邮箱',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '平台用户' ROW_FORMAT = Dynamic;
