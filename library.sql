/*
 Navicat Premium Data Transfer

 Source Server         : MYSQL
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 21/06/2023 21:35:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `total_copies` int NOT NULL,
  `available_copies` int NOT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '123', '123', '123', 5, 0, '2023-06-21 14:28:27');

-- ----------------------------
-- Table structure for borrow_queue
-- ----------------------------
DROP TABLE IF EXISTS `borrow_queue`;
CREATE TABLE `borrow_queue`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `queue_date` datetime(3) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of borrow_queue
-- ----------------------------
INSERT INTO `borrow_queue` VALUES (98, 9, 1, '2023-06-21 13:06:14.371');

-- ----------------------------
-- Table structure for borrow_records
-- ----------------------------
DROP TABLE IF EXISTS `borrow_records`;
CREATE TABLE `borrow_records`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `borrow_date` datetime(3) NOT NULL,
  `return_date` datetime(3) NULL DEFAULT NULL,
  `rating` int NULL DEFAULT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of borrow_records
-- ----------------------------
INSERT INTO `borrow_records` VALUES (63, 1, 1, '2023-06-21 10:45:25.440', '2023-06-21 10:45:25.526', 1, '123');
INSERT INTO `borrow_records` VALUES (64, 2, 1, '2023-06-21 10:45:25.452', '2023-06-21 13:18:34.156', NULL, '');
INSERT INTO `borrow_records` VALUES (65, 3, 1, '2023-06-21 10:45:25.461', NULL, NULL, NULL);
INSERT INTO `borrow_records` VALUES (66, 4, 1, '2023-06-21 10:45:25.470', NULL, NULL, NULL);
INSERT INTO `borrow_records` VALUES (67, 5, 1, '2023-06-21 10:45:25.478', NULL, NULL, NULL);
INSERT INTO `borrow_records` VALUES (68, 7, 1, '2023-06-21 10:45:25.535', NULL, NULL, NULL);
INSERT INTO `borrow_records` VALUES (69, 8, 1, '2023-06-21 13:18:34.379', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for library_info
-- ----------------------------
DROP TABLE IF EXISTS `library_info`;
CREATE TABLE `library_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `library_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `open_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `close_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of library_info
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 323 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (123, '123', '123', '123', '123', '2023-06-20 20:27:51', '123');
INSERT INTO `user` VALUES (321, '321', '321', '321', '321', '2023-06-20 20:28:18', '312');
INSERT INTO `user` VALUES (322, '陈航', '123456', 'o.yvuixqt@qq.com', '19829312158', '2023-11-10 16:00:00', NULL);

SET FOREIGN_KEY_CHECKS = 1;
