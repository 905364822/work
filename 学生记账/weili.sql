/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : weili

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-12-20 21:30:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `income` int(10) NOT NULL,
  `expanditure` int(10) NOT NULL,
  `initAmount` int(12) NOT NULL,
  `currAmount` int(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------

-- ----------------------------
-- Table structure for class table
-- ----------------------------
DROP TABLE IF EXISTS `class table`;
CREATE TABLE `class table` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `categoryId` int(10) NOT NULL,
  `study` int(10) NOT NULL,
  `recreation` int(10) NOT NULL,
  `shopping` int(10) NOT NULL,
  `work` int(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of class table
-- ----------------------------

-- ----------------------------
-- Table structure for classification number
-- ----------------------------
DROP TABLE IF EXISTS `classification number`;
CREATE TABLE `classification number` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `categoryId` int(4) NOT NULL,
  `categoryName` char(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of classification number
-- ----------------------------
