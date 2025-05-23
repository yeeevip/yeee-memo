## 框架说明

- 经过多年实战经验中积累的一套基于**SpringBoot**、**SpringCloud**的**JavaWeb通用脚手架**
- 核心设计思想是将web应用开发中的通用功能**抽象组件化**，从而达到**轻量级**、**可拓展**

## 项目案例

| 项目名称              | 描述           |                          仓库地址                          |
|:------------------|:-------------|:------------------------------------------------------:|
| yeee-blog         | 轻量级博客系统      |     [仓库](https://gitee.com/yeeevip/yeee-blog.git)     |     
| yeee-chatgpt      | GPT微信小程序     |    [仓库](https://gitee.com/yeeevip/yeee-chatgpt.git)    |    
| yeee-crowdfunding | 大学生众筹平台      | [仓库](https://gitee.com/yeeevip/yeee-crowdfunding.git) |
| yeee-activiti7    | activiti7工作流 | [仓库](https://gitee.com/yeeevip/yeee-activiti7.git) |

## 升级版：JDK17 + SpringBoot3

| 项目名称              | 描述                   |                          仓库地址                          |
|:------------------|:---------------------|:------------------------------------------------------:|
| Yeee-JavaWeb通用脚手架升级版         | JDK17 + SpringBoot3 |     [仓库](https://gitee.com/yeeevip/yeee-momoo)     | 

## 模块说明

---------------------------------
- 脚手架核心通用模块**memo-parent**，按功能抽取出来封装为通用的**starter**
---------------------------------

```lua
├ memo-parent
├
├── memo-dependencies --------------------------- Maven依赖版本统一管理
├
├── memo-base ----------------------------------- 基础抽象模块
    ├── base-config
    ├── base-jdbc
        ├── base-mybatis  
        ├── base-mybatis-plus ------------------- 包含Mp基础依赖、分页插件、字段填充
        └── base-tk-mapper
    ├── base-model ------------------------------ 基础模型，包含公共的VO、异常类、注解类
    ├── base-redis -- 
    ├── base-security-oauth2 -------------------- 认证授权基础，公共配置、依赖、常亮等
    ├── base-swagger ---------------------------- 基于knife4j的swagger文档框架，只需要引入依赖添加配置即可使用
    ├── base-util ------------------------------- 工具类，如hutool、json工具、guava等
    ├── base-web -------------------------------- 基础web模块，引入web项目必须的springboot-web-starter、校验、监控等
├    
└── memo-common --------------------------------- 在memo-base的基础上封装的*通用模块*
    ├── common-auth 
        ├── common-app-auth-client -------------- C端应用鉴权客户端
        ├── common-app-auth-server -------------- C端应用鉴权服务端
        ├── common-platform-auth-client --------- 管理端应用鉴权客户端
        └── common-platform-auth-server --------- 管理端应用鉴权客户端
    ├── common-dingtalk ------------------------- 钉钉开发接口通用功能，如聊天机器人通知等 
    ├── common-domain
    ├── common-httpclient
        └── common-httpclient-okhttp ------------ okhttp3通用封装
    ├── common-kit ------------------------------ 通用工具组件
        ├── common-excel-kit
        └── common-redisson-kit
    ├── common-oss ------------------------------ 基于策略模式的通用oss工具组件封装，如ali、七牛云等
    ├── common-springcloud 
        ├── common-springcloud-dependencies ----- Springcloud项目公共依赖，如服务配置、服务注册、负载均衡、限流熔断等
        ├── common-springcloud-gray ------------- springcloud服务灰度发布组件
            ├── common-springcloud-gray-common 
            ├── common-springcloud-gray-gateway - springcloud-gateway灰度发布依赖
            └── common-springcloud-gray-inner --- springcloud内部服务灰度发布依赖
        └── common-springcloud-openfeign
    ├── common-sso ------------------------------ 单点登录
        ├── common-sso-inner-app 
        └── common-sso-third-app
    ├── common-web ------------------------------ 通用web模块，在base-web基础上加入jdbc、swagger等，较完整的web-starter
    ├── common-websocket ------------------------ websocket 
    ├── common-websse --------------------------- websse
    ├── common-wxsdk
        ├── common-wx-ma ------------------------ 通用微信小程序组件
        └── common-wx-mp ------------------------ 通用微信公众号组件
    ├── common-workflow
        ├── common-activiti7 ------------------------ 工作流
    └── common-mybatis-encrypt ------------------ 借鉴其他开源封装的基于mybatis的数据库字段脱敏组件
```

---------------------------------
- 基于memo-parent脚手架的一些应用**Demo**，只需引入对应的**starter**即可实现功能
---------------------------------

```lua
├ 
├ learn-example --------------------------------- 临时学习测试
├
├ solution-problem ------------------------------ 解决方案
    ├── distribute-lock ------------------------- 分布式锁的不同实现方案
    ├── jetcache -------------------------------- 分布式二级缓存
    ├── netty
    ├── sub-database-table ---------------------- 分表分表
    ├── tokenizing ------------------------------ 分词
    ├── webservice-example 
    └── websocket-example
├ 
├ middle-ware ----------------------------------- 中间件使用
    ├── canal
    ├── elasticsearch --------------------------- 搜索
    ├── flink ----------------------------------- 大数据实时计算
    ├── mongodb --------------------------------- 高性能、海量分布式存储
    └── MQ -------------------------------------- 消息队列
├ 
├ spring-cloud ---------------------------------- springcloud的一些demo
    ├── auth-sso -------------------------------- 身份认证、资源授权
    ├── config ---------------------------------- 配置中心
    ├── gateway --------------------------------- springcloud-gateway网关
    ├── protect --------------------------------- 服务限流、降级熔断保护
    ├── register -------------------------------- 注册中心
    ├── rpc ------------------------------------- RPC远程服务调用
    └── transaction ----------------------------- 分布式事务
├ 
├ third-sdk ------------------------------------- 三方SDK
    ├── aliyun-sdk
    ├── blockchain ------------------------------ 区块链
    ├── third-pay ------------------------------- 基于策略模式封装统一支付DEMo，包含微信、支付宝各种支付方式的统一处理
    └── weixin-sdk
```

## 快速开始

### 快速搭建基于Netty的Websocket应用程序

1. 下载yeee-memo工程，打包构建JavaWeb通用脚手架**memo-parent**

```
git clone https://github.com/yeeevip/yeee-memo.git
cd memo-parent && mvn clean install
```

2. pom文件引入**common-netty-websocket**通用依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- yeee-memo脚手架 -->
    <parent>
        <artifactId>memo-parent</artifactId>
        <groupId>vip.yeee.memo</groupId>
        <version>3.2.2-SNAPSHOT</version>
        <relativePath/>
    </parent>
    <artifactId>api-auth-server</artifactId>
    <dependencies>
        <dependency>
            <groupId>vip.yeee.memo</groupId>
            <artifactId>common-web</artifactId>
        </dependency>
        <!-- [netty-websocket]通用依赖 -->
        <dependency>
            <groupId>vip.yeee.memo</groupId>
            <artifactId>common-netty-websocket</artifactId>
        </dependency>
    </dependencies>
</project>
```

3. 创建端点控制器类，类似Controller，就可以实现自己的业务逻辑

```java
@ServerEndpoint(path = "/ws/{arg}")
public class MyWebSocket {

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathParam String arg, @PathParam Map pathMap) {
        ...
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        ...
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        ...
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        ...
    }
}
```    

4. 最后一个Netty-Websocket应用就搭建好了

- 完整代码请参考：[yeee-chatgpt](https://gitee.com/yeeevip/yeee-chatgpt.git)

### 基于memo脚手架快速搭建Spring+Security+Oauth2的认证/授权服务器

1. 下载yeee-memo工程，打包构建JavaWeb通用脚手架**memo-parent**

```
git clone https://github.com/yeeevip/yeee-memo.git
cd memo-parent && mvn clean install
```

2. 使用IDEA新建maven工程web-auth-server，pom文件引入**common-platform-auth**通用依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- yeee-memo脚手架 -->
    <parent>
        <artifactId>memo-parent</artifactId>
        <groupId>vip.yeee.memo</groupId>
        <version>3.2.2-SNAPSHOT</version>
        <relativePath/>
    </parent>
    <artifactId>api-auth-server</artifactId>
    <dependencies>
        <dependency>
            <groupId>vip.yeee.memo</groupId>
            <artifactId>common-web</artifactId>
        </dependency>
        <!-- [授权/认证服务器]通用依赖 -->
        <dependency>
            <groupId>vip.yeee.memo</groupId>
            <artifactId>common-platform-auth-server</artifactId>
        </dependency>
        <!-- [资源客户端]通用依赖 -->
        <dependency>
            <groupId>vip.yeee.memo</groupId>
            <artifactId>common-platform-auth-client</artifactId>
        </dependency>
    </dependencies>
</project>
```

3. 继承AbstractCustomUserDetailsService重写getSystemUserByUsername方法和自己系统用户关联起来

```java
public class CustomUserDetailsService extends AbstractCustomUserDetailsService {
    
    @Override
    public AuthUser getUserByUserTypeAndUsername(String username) {
        SysUser sysUser = sysUserMapper.selectOne(username);
        if (sysUser == null) {
            throw new BizException(MessageConstant.USER_NOT_EXIST);
        }
        ...
        SystemUserBo userBo = new SystemUserBo();
        ...
        return userBo;
    }
}
```

4. 在application.yml中添加以下配置

```
security:
  oauth2:
    resource:
      token-info-uri: http://cloud-web-auth-server/auth-server/oauth/check_token
    client:
      client-id: ${spring.application.name}
      client-secret: 123456
      #      grant-type: password
      access-token-uri: http://cloud-web-auth-server/auth-server/oauth/token
yeee:
  auth:
    resource:
      # 设置排除鉴权的URL
      exclude:
        - /system/register
        - /system/login
        - /anonymous/limit/api
```

5. 最后一个web认证服务器就搭建好了

- 完整代码请参考：[web-auth-server](https://gitee.com/yeeevip/yeee-memo/tree/master/spring-cloud/auth-sso/web-auth-server)

## 技术交流群

- 作者QQ：394230080
- 招有经验尽量会全栈的技术，有意向的可以来我们**接单群**

| 微信群                                     | QQ群                                                                                | 技术接单群                                                                           |
|-----------------------------------------|------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| ![作者微信](doc/images/yeee_wp.jpg)  | ![](doc/images/yeee-memo-qq.jpg)                                               | ![](doc/images/yeee_wx.jpg)
| 回复：加群，邀您加入群聊                   | <a href="https://qm.qq.com/q/oLSCm1Ksjm" target="_blank">点击加入QQ群：96305921</a>  |java、vue、微信小程序接单

