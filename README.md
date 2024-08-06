<h1 align="center">API接口开放平台</h1>
<p align="center"><strong>API 接口开放平台是一个为用户和开发者提供全面API接口调用服务的平台 🛠</strong></p>
<div align="center">
<a>
<img alt="Maven" src="https://raster.shields.io/badge/Maven-3.6.3-red.svg"/>
</a>
<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img alt="" src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
</a>
    <img alt="SpringBoot" src="https://raster.shields.io/badge/SpringBoot-2.7+-green.svg"/>
</div>

## 项目介绍 🙋
基于React+Spring Boot+Dubbo+Gateway的API接口开放调用平台。管理员可以接入并发布接口，分析各接口调用情况；用户可以开通接口调用权限、浏览接口及在线调试，开发者可以通过客户端SDK轻松调用接口。
## 项目导航 🧭
- [**API 后端 🏘️**](https://github.com/GoodLuckyed/api-project)
- [**API 前端 🏘**️](https://github.com/GoodLuckyed/api-frontend)
- [**API-SDK 🔥**](https://github.com/GoodLuckyed/api-sdk) 
## 项目结构 📑
| 目录               | 描述 |
|------------------| --- |
| 🏘️ api- backend | API后端服务模块 |
| 🏘️ api-common   | 公共服务模块 |
| 🔗 api-interface | 接口模块 |
| 🕸️ api-gateway  | 网关模块 |
| 🔥 api-sdk      | 开发者调用sdk |

## 项目选型 🎯
### 后端
- Spring Boot 2.7.6
- Spring MVC
- MySQL 数据库
- 腾讯云COS存储
- Dubbo 分布式（RPC、Nacos）
- Spring Cloud Gateway 微服务网关
- API 签名认证（Http 调用）
- AliPay  支付宝支付
- Swagger + Knife4j 接口文档
- Spring Boot Starter（SDK 开发）
- Spring Session Redis 分布式登录
- Apache Commons Lang3 工具类
- MyBatis-Plus 及 MyBatis X 自动生成
- Hutool、Apache Common Utils、Gson 等工具库

### 前端

- React 18

- Ant Design Pro 5.x 脚手架

- Ant Design & Procomponents 组件库

- Umi 4 前端框架

- OpenAPI 前端代码生成

## 功能展示 ✨
### 首页
![首页](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/index.png)
### 接口广场
![接口广场](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/square.png)
### 接口详情
![接口详情](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/detail.png)
### 在线调试
![在线调试](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/debug.png)
### 购买积分
![购买积分](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/mall.png)
### 支付
![支付](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/pay.png)
### 订单
![订单](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/order.png)
### 接口管理
![接口管理](https://github.com/GoodLuckyed/api-frontend/blob/master/public/images/admin.png)