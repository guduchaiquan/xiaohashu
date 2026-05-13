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

## 前端运行

```bash
cd xiaohashu-web
npm install
npm run dev
```

默认访问 Vite 开发服务器，页面风格偏小红书，可用于登录、注册和笔记发布的交互测试。

## 后端说明

后端采用多模块 Maven 工程，当前已完成基础业务骨架和核心服务接口，后续可继续补齐完整联调、接口测试和页面接入。
