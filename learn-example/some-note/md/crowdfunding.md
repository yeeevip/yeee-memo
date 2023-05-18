

@[TOC](文章目录)

---

# 一、基本介绍

本项目是一套基于JavaWeb基础语言开发的众筹平台系统，无额外三方框架，有助于web项目基础原理学习实战；
项目整体包含前、后台两大模块，功能上完整涵盖众筹平台所需要的核心功能，
如从前台用户注册-登录-发起众筹-支持项目-个人中心，再到后台的项目管理审核-用户管理-订单管理，实现了完整的功能流程闭环。
同时项目结构分层清晰易懂，程序基于轻量化的设计理念没有额外无关的依赖项，运行方便简单。

---

适用于计算机相关专业学生或者没有实战项目经验的初级开发者：  
- 用于学期课程设计参考学习
- 用于毕业设计参考学习
- 需要实战项目练习的初级开发者

---

# 二、技术设计

`提示：安装运行过程中遇到问题可以联系我的 QQ：1324459373，QQ群：69405215`

## 1. 后台

- 开发语言：Java（Jdk1.8）
- 数据库：Mysql
- 基础框架：jdbc + servlet + io
- 权限安全：使用servlet的filter实现
- 开发软件：eclipse/idea

## 2. 前台

- jsp + html + js + jquery + ajax

---

# 三、运行步骤

## 1. 环境前提

- Jdk1.8
- Mysql，版本要求 >= 5.7，可视化程序建议使用Navicat
- Maven >= 3.6
- Tomcat
- 
## 2. 运行程序

`提示：安装运行过程中遇到问题可以联系我的 QQ：1324459373，QQ群：69405215`

1. 用Navicat连接本地数据库，新建crowdfunding数据库，然后将 /sql 目录下的脚本执行导入数据库
2. 将程序源代码导入编译器，进行构建编译
3. 修改项目配置文件数据库用户名、密码
4. 最后使用Tomcat启动程序即可

---

# 四、功能概述

## 1. 前台

> 注册 - 登录 - 发起项目 - 个人中心 - 我的项目 - 我的支持 

### 1. 注册登录

### 2. 发起项目

#### 1. 基础信息

![image](http://oos.yeee.vip/1.%E5%8F%91%E8%B5%B7%E9%A1%B9%E7%9B%AE-%E5%9F%BA%E7%A1%80%E4%BF%A1%E6%81%AF.png)

#### 2. 项目信息

![image](http://oos.yeee.vip/2.%E5%8F%91%E8%B5%B7%E9%A1%B9%E7%9B%AE-%E9%A1%B9%E7%9B%AE%E4%BF%A1%E6%81%AF.png)

#### 3. 项目详情

![image](http://oos.yeee.vip/3.%E5%8F%91%E8%B5%B7%E9%A1%B9%E7%9B%AE-%E9%A1%B9%E7%9B%AE%E8%AF%A6%E6%83%85.png)

#### 4. 回报设置

![image](http://oos.yeee.vip/4.%E5%8F%91%E8%B5%B7%E9%A1%B9%E7%9B%AE-%E5%9B%9E%E6%8A%A5%E8%AE%BE%E7%BD%AE.png)

#### 5. 编辑预览

![image](http://oos.yeee.vip/5.%E5%8F%91%E8%B5%B7%E9%A1%B9%E7%9B%AE-%E7%BC%96%E8%BE%91%E9%A2%84%E8%A7%88.png)

### 二、查看项目

#### 1.项目详情

![image](http://oos.yeee.vip/13.%E6%B5%8B%E8%AF%95%E9%A1%B9%E7%9B%AE%20-%E8%AF%A6%E6%83%85%E9%A1%B5.png)

### 3. 个人中心

#### 1. 我的发起

![image](http://oos.yeee.vip/6.%E4%B8%AA%E4%BA%BA%E4%BF%A1%E6%81%AF-%E6%88%91%E7%9A%84%E5%8F%91%E8%B5%B7.png)

#### 2. 我的订单

![image](http://oos.yeee.vip/14.%E4%B8%AA%E4%BA%BA%E4%BF%A1%E6%81%AF-%E6%88%91%E7%9A%84%E8%AE%A2%E5%8D%95.png)

#### 3. 更新项目动态

![image](http://oos.yeee.vip/11.%E5%8F%91%E8%B5%B7%E8%80%85%E6%9B%B4%E6%96%B0%E9%A1%B9%E7%9B%AE%E5%8A%A8%E6%80%81.png)

#### 4. 更新后详情页面

![image](http://oos.yeee.vip/12.%E6%9B%B4%E6%96%B0%E8%BF%9B%E5%B1%95%E5%90%8E.png)

## 2. 后台

> 用户管理 - 项目管理+审核 - 订单管理
> 
- 页面地址：http://localhost:9080/admin
- 管理员账号：admin，密码：111111，也可以后台自己添加账号

### 1. V1版本

#### 1. 管理员登录

![image](http://oos.yeee.vip/7.%E7%AE%A1%E7%90%86%E5%91%98%E7%99%BB%E9%99%86%E9%A1%B5%E9%9D%A2.png)

#### 2. 待审核项目

![image](http://oos.yeee.vip/8.%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E4%B8%AD%E5%BF%83-%E5%BE%85%E5%AE%A1%E6%A0%B8%E9%A1%B9%E7%9B%AE.png)

#### 3. 项目审核页面

![image](http://oos.yeee.vip/9.%E9%A1%B9%E7%9B%AE%E5%AE%A1%E6%A0%B8%E9%A1%B5%E9%9D%A2.png)

#### 4. 通过审核

![image](http://oos.yeee.vip/10.%E9%80%9A%E8%BF%87%E5%AE%A1%E6%A0%B8.png)

### 2. V2版本

---

# 五、源码下载

[现在去下载](https://download.csdn.net/download/qq13754113543/85771047)