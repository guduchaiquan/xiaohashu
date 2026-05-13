# 小哈书（xiaohashu）

小哈书是一个基于 Spring Cloud Alibaba 的仿小红书微服务项目，主要覆盖了用户、认证、笔记、对象存储、键值内容存储和分布式 ID 生成等核心能力。

## 项目定位

这个项目的目标是实现一个完整的内容社区后端基础能力，支持：

- 用户注册、登录、权限控制
- 笔记发布、内容存储、话题关联
- 图片/视频上传
- 短文本内容的分布式存储
- 全局唯一 ID 生成
- 网关统一鉴权与请求透传

## 模块结构

- `xiaohashu-gateway`：统一网关，负责鉴权、路由、Header 透传
- `xiaohashu-auth`：认证服务，负责登录、验证码、Token 处理
- `xiaohashu-user`：用户服务，负责用户资料、角色、权限
- `xiaohashu-note`：笔记服务，负责发布笔记、话题关联、内容编排
- `xiaohashu-oss`：对象存储服务，支持 MinIO / 阿里云 OSS
- `xiaohashu-kv`：键值服务，负责笔记长内容存储
- `xiaohashu-distribute-id-generator`：分布式 ID 生成服务
- `xiaoha-framework`：公共基础组件与 Starter

## 技术栈

- Java 17
- Spring Boot 3
- Spring Cloud Alibaba
- MyBatis
- Redis
- Nacos
- MySQL
- Cassandra
- MinIO / 阿里云 OSS
- Sa-Token

## 本地运行前准备

运行前请先准备以下基础设施：

- MySQL
- Redis
- Nacos
- Cassandra（KV 服务使用）
- MinIO 或阿里云 OSS（对象存储）
- 需要的话还要准备短信服务、Zookeeper 等外部依赖

## 配置说明

当前仓库中的 `application-dev.yml` 已将敏感信息移除，请通过环境变量或本地私有配置注入：

- Redis 密码
- MySQL 密码
- 阿里云 AccessKey
- 对象存储密钥

建议方式：

- 本地开发使用未提交到仓库的 `application-local.yml`
- 或直接用环境变量覆盖配置项

## 启动顺序建议

1. 启动 Nacos
2. 启动 MySQL / Redis / Cassandra / MinIO
3. 启动 `xiaohashu-distribute-id-generator`
4. 启动 `xiaohashu-kv`
5. 启动 `xiaohashu-user`
6. 启动 `xiaohashu-auth`
7. 启动 `xiaohashu-note`
8. 启动 `xiaohashu-gateway`

## 当前已知问题

- 部分模块仍依赖外部服务，缺少完整的联调环境时无法直接跑通
- 配置文件仍需要按本地环境补齐
- 部分业务接口还缺少完整的前端页面和端到端测试

## 后续可以继续完善的方向

- 补齐更完整的接口文档
- 增加统一的本地启动配置样例
- 增加单元测试和集成测试
- 统一异常码、返回体、日志结构
- 增加首页推荐、关注流、点赞评论收藏等能力

## 说明

本项目为学习和实战性质的微服务项目，适合用于研究 Spring Cloud Alibaba 的服务拆分、RPC 调用和业务分层设计。
