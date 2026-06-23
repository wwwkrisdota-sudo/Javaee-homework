# 直播打赏微服务系统

JavaEE架构与应用课程设计 — 后端项目（Spring Boot + Spring Cloud + MySQL + Nacos）

## 项目结构

```
live-tip-platform/
├── docs/项目分工说明.md          # 四人分工与接口规范
├── sql/
│   ├── schema.sql               # 表结构
│   └── init_data.sql            # 初始化数据（100主播 + 1000观众）
├── live-tip-common/             # 公共模块
├── live-tip-gateway/            # API 网关 :8080
├── live-tip-audience-service/   # 观众服务 :8081
├── live-tip-finance-service/    # 财务服务 :8082
├── live-tip-analytics-service/  # 经营分析 :8083
└── live-tip-mock-service/       # 模拟压测 :8084
```

## 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Nacos 2.x（注册中心，默认 `127.0.0.1:8848`）

## 快速启动

### 1. 初始化数据库

```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/init_data.sql
```

根据本地环境修改各服务 `application.yml` 中的数据库账号密码。

### 2. 启动 Nacos

下载并启动 Nacos Standalone 模式，确保控制台可访问 `http://127.0.0.1:8848/nacos`。

### 3. 编译项目

```bash
mvn clean install -DskipTests
```

### 4. 启动各服务（建议顺序）

```bash
# 业务服务
mvn -pl live-tip-finance-service spring-boot:run
mvn -pl live-tip-audience-service spring-boot:run
mvn -pl live-tip-analytics-service spring-boot:run
mvn -pl live-tip-mock-service spring-boot:run

# 网关
mvn -pl live-tip-gateway spring-boot:run
```

### 5. 健康检查

```bash
curl http://localhost:8080/api/audience/health
curl http://localhost:8080/api/finance/health
curl http://localhost:8080/api/analytics/health
curl http://localhost:8080/api/mock/health
```

## 第一部分完成情况（成员一）

- [x] MySQL 表结构设计（9 张核心表）
- [x] Spring Cloud 多模块 Maven 父工程
- [x] 公共模块：统一响应、异常、TraceId、HTTP 日志切面、实体类
- [x] 网关路由与 TraceId 透传
- [x] 四个业务服务空壳（可启动、可注册 Nacos）
- [x] 四人分工说明文档

## 后续开发

详见 [docs/项目分工说明.md](docs/项目分工说明.md)。
