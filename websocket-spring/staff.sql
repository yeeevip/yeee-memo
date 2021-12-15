/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : sakila

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 10/08/2020 15:35:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `staff_id` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address_id` smallint(5) UNSIGNED NOT NULL,
  `picture` blob,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `store_id` tinyint(3) UNSIGNED NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `username` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `last_update` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`staff_id`) USING BTREE,
  INDEX `idx_fk_store_id`(`store_id`) USING BTREE,
  INDEX `idx_fk_address_id`(`address_id`) USING BTREE,
  CONSTRAINT `fk_staff_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_staff_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES (1, 'Mike', 'Hillyer', 3, NULL, '', 1, 1, 'Mike', '81dc9bdb52d04dc20036dbd8313ed055', '2017-06-29 14:27:38');
INSERT INTO `staff` VALUES (2, 'Jon', 'Stephens', 4, NULL, '', 2, 1, 'Jon', '1234', '2017-06-29 14:27:38');
INSERT INTO `staff` VALUES (3, 'TOM', 'DSA', 3, NULL, 'DF', 1, 1, 'TOM', '1234', '2016-11-11 11:08:10');

SET FOREIGN_KEY_CHECKS = 1;
