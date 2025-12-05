# GeoTask - 地理任务管理系统

基于Spring Boot + MyBatis + PostgreSQL的地理任务管理系统，支持任务的增删改查和地理位置相关功能。

## 项目结构

```
src/main/java/org/syj/geotask/
├── GeoTaskApplication.java          # 启动类
├── task/
│   ├── entity/
│   │   └── Task.java                # 任务实体类
│   ├── dao/
│   │   └── TaskMapper.java          # 数据访问层接口
│   ├── service/
│   │   ├── TaskService.java         # 服务层接口
│   │   └── impl/
│   │       └── TaskServiceImpl.java # 服务层实现
│   └── controller/
│       └── TaskController.java      # 控制器层
└── resources/
    ├── application.properties        # 配置文件
    ├── mapper/
    │   └── TaskMapper.xml           # MyBatis映射文件
    └── sql/
        └── task_table.sql           # 数据库表创建脚本
```

## 功能特性

### 基础CRUD操作
- ✅ 创建任务
- ✅ 根据ID查询任务
- ✅ 更新任务
- ✅ 删除任务
- ✅ 查询所有任务

### 高级查询功能
- ✅ 根据完成状态查询任务
- ✅ 根据标题模糊搜索任务
- ✅ 根据时间范围查询任务
- ✅ 根据地理位置范围查询任务

### 任务管理功能
- ✅ 标记任务完成/未完成
- ✅ 切换任务完成状态
- ✅ 启用/禁用任务提醒
- ✅ 获取任务统计信息

## 数据库配置

### 1. 创建数据库
```sql
CREATE DATABASE geotask;
```

### 2. 执行表创建脚本
执行 `src/main/resources/sql/task_table.sql` 文件中的SQL语句创建任务表。

### 3. 配置数据库连接
在 `application.properties` 中配置PostgreSQL连接信息：
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/geotask
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## API接口文档

### 基础CRUD接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/tasks` | 创建任务 |
| GET | `/api/tasks/{id}` | 根据ID获取任务 |
| PUT | `/api/tasks/{id}` | 更新任务 |
| DELETE | `/api/tasks/{id}` | 删除任务 |
| GET | `/api/tasks` | 获取所有任务 |

### 查询接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/tasks/completed/{isCompleted}` | 根据完成状态查询 |
| GET | `/api/tasks/search?title=关键字` | 根据标题搜索 |
| GET | `/api/tasks/date-range?startDate=开始时间&endDate=结束时间` | 时间范围查询 |
| GET | `/api/tasks/location?latitude=纬度&longitude=经度&radius=半径` | 地理位置查询 |

### 任务管理接口

| 方法 | 路径 | 描述 |
|------|------|------|
| PUT | `/api/tasks/{id}/complete` | 标记为已完成 |
| PUT | `/api/tasks/{id}/uncomplete` | 标记为未完成 |
| PUT | `/api/tasks/{id}/toggle` | 切换完成状态 |
| PUT | `/api/tasks/{id}/enable-reminder` | 启用提醒 |
| PUT | `/api/tasks/{id}/disable-reminder` | 禁用提醒 |
| GET | `/api/tasks/statistics` | 获取统计信息 |

## 请求示例

### 创建任务
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "完成项目文档",
    "description": "编写项目的技术文档",
    "dueDate": 1704067200000,
    "dueTime": 1704067200000,
    "location": "北京市朝阳区",
    "latitude": 39.9042,
    "longitude": 116.4074,
    "geofenceRadius": 500
  }'
```

### 查询所有任务
```bash
curl -X GET http://localhost:8080/api/tasks
```

### 根据地理位置查询
```bash
curl -X GET "http://localhost:8080/api/tasks/location?latitude=39.9042&longitude=116.4074&radius=1000"
```

## 运行项目

### 1. 启动PostgreSQL数据库
确保PostgreSQL服务正在运行，并已创建`geotask`数据库。

### 2. 执行数据库脚本
```bash
psql -U postgres -d geotask -f src/main/resources/sql/task_table.sql
```

### 3. 启动应用
```bash
mvn spring-boot:run
```

### 4. 访问应用
应用将在 `http://localhost:8080` 启动。

## 技术栈

- **框架**: Spring Boot 3.5.8
- **数据库**: PostgreSQL
- **ORM**: MyBatis 3.0.3
- **构建工具**: Maven
- **Java版本**: 17
- **其他**: Lombok, Spring Web

## 实体类字段说明

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 主键ID，自动生成 |
| title | String | 任务标题（必填） |
| description | String | 任务描述 |
| dueDate | Long | 截止日期（时间戳） |
| dueTime | Long | 截止时间（时间戳） |
| isCompleted | Boolean | 是否已完成 |
| isReminderEnabled | Boolean | 是否启用提醒 |
| location | String | 地址描述 |
| latitude | Double | 纬度 |
| longitude | Double | 经度 |
| geofenceRadius | Float | 地理围栏半径（米） |
| createdAt | Long | 创建时间（时间戳） |
| updatedAt | Long | 更新时间（时间戳） |

## 注意事项

1. 确保PostgreSQL数据库已正确安装和配置
2. 数据库连接信息需要根据实际环境修改
3. 地理位置查询使用了Haversine公式计算距离
4. 所有时间字段使用Unix时间戳（毫秒）
5. 项目使用了Lombok，需要IDE安装相应插件
