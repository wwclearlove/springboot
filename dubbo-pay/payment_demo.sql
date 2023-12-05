/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : payment_demo

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 09/11/2022 10:39:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order_info
-- ----------------------------
DROP TABLE IF EXISTS `t_order_info`;
CREATE TABLE `t_order_info`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单标题',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户订单编号',
  `payment_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '支付产品id',
  `total_fee` int(11) NULL DEFAULT NULL COMMENT '订单金额(分)',
  `code_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单二维码连接',
  `order_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 139 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_info
-- ----------------------------
INSERT INTO `t_order_info` VALUES (139, 'Java课程', 'ORDER_20221109103201111', '微信', NULL, 1, 1, 'weixin://wxpay/bizpayurl?pr=PC8x77vzz', '用户已取消', '2022-11-09 10:32:01', '2022-11-09 10:32:50');
INSERT INTO `t_order_info` VALUES (140, 'Java课程', 'ORDER_20221109103252814', '微信', NULL, 1, 1, 'weixin://wxpay/bizpayurl?pr=65v3wRYzz', '已退款', '2022-11-09 10:32:52', '2022-11-09 10:35:28');

-- ----------------------------
-- Table structure for t_payment_info
-- ----------------------------
DROP TABLE IF EXISTS `t_payment_info`;
CREATE TABLE `t_payment_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付记录id',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户订单编号',
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付系统交易编号',
  `payment_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付类型',
  `trade_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易类型',
  `trade_state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易状态',
  `payer_total` int(11) NULL DEFAULT NULL COMMENT '支付金额(分)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知参数',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_payment_info
-- ----------------------------
INSERT INTO `t_payment_info` VALUES (52, 'ORDER_20221109103252814', '4200001669202211097721618822', '微信', 'NATIVE', 'SUCCESS', 1, '{transaction_id=4200001669202211097721618822, amount={total=1.0, payer_total=1.0, currency=CNY, payer_currency=CNY}, mchid=1558950191, out_trade_no=ORDER_20221109103252814, trade_state=SUCCESS, bank_type=OTHERS, appid=wx74862e0dfcf69954, trade_state_desc=支付成功, trade_type=NATIVE, attach=微信系开发脚手架, success_time=2022-11-09T10:33:05+08:00, payer={openid=oHwsHuO16XOrZ90d7KXZhk1Q14Og}}', '2022-11-09 10:33:08', '2022-11-09 10:33:08');

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` int(11) NULL DEFAULT NULL COMMENT '价格（分）',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, 'Java课程', 1, '2021-12-08 00:51:26', '2021-12-10 00:21:37');
INSERT INTO `t_product` VALUES (2, '大数据课程', 1, '2021-12-08 00:51:26', '2021-12-10 00:21:38');
INSERT INTO `t_product` VALUES (3, '前端课程', 1, '2021-12-08 00:51:26', '2022-04-11 07:55:00');
INSERT INTO `t_product` VALUES (4, 'UI课程', 1, '2021-12-08 00:51:26', '2021-12-10 00:21:39');

-- ----------------------------
-- Table structure for t_refund_info
-- ----------------------------
DROP TABLE IF EXISTS `t_refund_info`;
CREATE TABLE `t_refund_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '退款单id',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户订单编号',
  `refund_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户退款单编号',
  `refund_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付系统退款单号',
  `total_fee` int(11) NULL DEFAULT NULL COMMENT '原订单金额(分)',
  `refund` int(11) NULL DEFAULT NULL COMMENT '退款金额(分)',
  `reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因',
  `refund_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款状态',
  `content_return` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '申请退款返回参数',
  `content_notify` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '退款结果通知参数',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_refund_info
-- ----------------------------
INSERT INTO `t_refund_info` VALUES (28, 'ORDER_20221109103252814', 'REFUND_20221109103408082', '50301803782022110926958697907', 1, 1, '买错了', 'SUCCESS', '{transaction_id=4200001669202211097721618822, amount={currency=CNY, discount_refund=0.0, from=[], payer_refund=1.0, payer_total=1.0, refund=1.0, refund_fee=0.0, settlement_refund=1.0, settlement_total=1.0, total=1.0}, out_trade_no=ORDER_20221109103252814, create_time=2022-11-09T10:34:08+08:00, promotion_detail=[], out_refund_no=REFUND_20221109103408082, funds_account=AVAILABLE, channel=ORIGINAL, user_received_account=支付用户零钱通, refund_id=50301803782022110926958697907, status=PROCESSING}', '{transaction_id=4200001669202211097721618822, amount={total=1.0, refund=1.0, payer_total=1.0, payer_refund=1.0}, mchid=1558950191, out_trade_no=ORDER_20221109103252814, refund_status=SUCCESS, out_refund_no=REFUND_20221109103408082, success_time=2022-11-09T10:34:16+08:00, user_received_account=支付用户零钱通, refund_id=50301803782022110926958697907}', '2022-11-09 10:34:08', '2022-11-09 10:35:28');

SET FOREIGN_KEY_CHECKS = 1;
