

一、后台
使用spring-security+oauth2实现 资源服务器1登录后可以在访问其他服务允许的资源（仅一次登录SSO）

spring-cloud
    - auth-sso
        - web-auth-server        认证服务器
        - web-resource-server1   资源服务器1
        - web-resource-server2   资源服务器2

二、C端应用
基于JWT实现

common-auth
    - common-app-auth-client
    - common-app-auth-server