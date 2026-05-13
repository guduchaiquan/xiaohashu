# 小哈书

小哈书是一个基于 Spring Cloud Alibaba 的仿小红书风格微服务项目，包含认证、用户、笔记、对象存储、KV 存储、分布式 ID 生成和网关等模块。

## 当前模块

- `xiaohashu-auth`：认证、登录、验证码
- `xiaohashu-user`：用户资料、权限、角色
- `xiaohashu-note`：笔记发布与内容管理
- `xiaohashu-oss`：文件上传与对象存储
- `xiaohashu-kv`：笔记正文等长文本存储
- `xiaohashu-distribute-id-generator`：分布式 ID 生成
- `xiaohashu-gateway`：统一网关与鉴权
- `xiaoha-framework`：公共基础组件
- `xiaohashu-web`：Vue 3 前端测试页面
- `xiaohashu-agent`：AI 创作助手服务（标题生成/正文润色/标签推荐）

## 后端接口测试顺序

建议先启动 `Nacos`、`Redis`、`MySQL`，再启动业务服务。

### 1. 发送验证码

`POST /verification/code/send`

```json
{
  "phone": "13800138000"
}
```

### 2. 注册用户

`POST /user/register`

```json
{
  "phone": "13800138000"
}
```

### 3. 查询手机号是否存在

`POST /user/findByPhone`

```json
{
  "phone": "13800138000"
}
```

### 4. 验证码登录

`POST /user/login`

```json
{
  "phone": "13800138000",
  "type": 1,
  "code": "123456"
}
```

### 5. 密码登录

`POST /user/login`

```json
{
  "phone": "13800138000",
  "type": 2,
  "password": "123456"
}
```

### 6. 发布笔记

`POST /note/publish`

```json
{
  "type": 1,
  "title": "我的第一篇笔记",
  "content": "这是笔记正文",
  "imgUris": [
    "https://xx.com/a.jpg",
    "https://xx.com/b.jpg"
  ],
  "topicId": 1
}
```

## Postman 测试建议

### 通用配置

- `base_url`：例如 `http://localhost:8000`
- 登录成功后把返回的 token 保存到环境变量 `token`

### 请求头

如果你走的是网关或者需要登录态的接口，常见需要加：

```http
Authorization: Bearer {{token}}
```

具体以你当前项目网关/拦截器实现为准。

### 推荐测试流程

1. 调用验证码发送接口
2. 调用注册接口
3. 调用手机号查询接口
4. 调用验证码登录接口
5. 调用密码登录接口
6. 调用发布笔记接口

## 前端运行

```bash
cd xiaohashu-web
npm install
npm run dev
```

默认访问 Vite 开发服务器，页面风格偏小红书，可用于登录、注册、笔记发布和 AI 创作测试。

## Agent 服务运行

```bash
mvn -pl xiaohashu-agent spring-boot:run
```

Agent 服务默认端口为 `8090`，提供如下接口：

- `POST /agent/title`：生成标题
- `POST /agent/rewrite`：正文润色
- `POST /agent/tags`：标签推荐

## 后端说明

后端采用多模块 Maven 工程，当前已完成基础业务骨架和核心业务接口，后续可继续补齐完整联调、接口测试和页面接入。
