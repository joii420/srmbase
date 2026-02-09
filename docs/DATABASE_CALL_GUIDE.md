# DATABASE_CALL_GUIDE

> 目标读者：供 AI 调用与维护者参考的机器可读数据库调用规范（面向 `session` 与 `param` 下的公有 API）。

目录
- 概述
- 约定与安全边界（AI 使用限制）
- 受支持接口清单（文件/类/方法签名）
- 参数/选项/枚举说明
- 会话与事务生命周期模板（2 种：try-with-resources / try-catch-finally）
- 五个 AI 可直接调用的 Java 示例模板（含输入/输出/异常契约）
- 两个验证/测试建议（JUnit 模板说明）
- 参考源码清单（相对路径）


## 概述

本规范基于仓库中的实现（主要是 `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session` 与 `.../param` 目录）。文档目的：
- 为自动化代理（AI）和维护者提供受支持、可安全调用的数据库 API 契约；
- 明确参数、返回值、异常与资源管理约定；
- 提供可被程序化调用的示例模板与验证建议，以供 CI 或 AI 在自动环境下使用。

要点摘要：
- 主力类型：`JdbcSession`, `JdbcSessionPool`, `BaseTransactionJdbcSession`, `JdbcSessionBuilder`, `JdbcParam`, `JdbcOption`, `UserTransaction`。
- SQL 参数占位符模式：`#key#`（见 `BaseJdbcSession.formatSqlParam` 实现），在替换时会对字符串单引号进行转义（`'` -> `''`），并支持 `LocalDate` / `Date` / `Boolean` / `Number`。
- 默认 DBType：若 `JdbcParam` 未设置 `dbType`，默认为 `DBType.POSTGRESQL`。
- 写操作（INSERT）若 SQL 不包含 `RETURNING`，系统会在执行前自动添加 `RETURNING *`（见 `insert(...)` 行为），并通过 `ResultSet` 返回插入后的记录（若驱动支持）。


## 约定与安全边界（AI 使用限制）

1. 禁止未经消毒的动态字符串拼接：
   - 优先使用 `#key#` 参数占位并通过 `Map<String,Object>` 传参（`formatSqlParam` 会替换并进行最基本的转义）。
   - 对于更复杂/安全的需求（例如二进制/大文本/预编译语句），应当由人工或更低层的驱动来处理，因为当前实现使用 `Statement` 执行原始 SQL。
2. 事务边界需显式管理：对于需要事务隔离的多步操作，使用 `BaseTransactionJdbcSession`（`option.transaction = true`）并在操作后调用 `commit()` 或在异常时 `rollback()`。
3. 连接/会话必须在使用完成后归还或关闭：
   - 若通过 `JdbcSessionPool.getSession()` 获取会话，必须调用 `JdbcSessionPool.releaseSession(session)` 或使用 `useSession(...)` 回收。 
   - 对于直接调用 `JdbcSessionBuilder.buildSession(...)` 得到的 `JdbcSession`，必须调用 `close()`。
4. 输入校验：
   - `queryCount/update/execute` 等方法有基本 SQL 类型验证（例如 `queryCount` 只接受 `SELECT`，`update` 不接受 `SELECT`）。AI 必须遵守这些语义以避免抛错。 


## 受支持接口清单（按文件、类、方法列出）

说明：以下列出库中公有/重要方法签名与语义说明（提取自源码）。若后续源码变更，AI 应以代码为准。相对路径均基于仓库根目录。

### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionBuilder.java`
- public static Connection initConnection(JdbcParam jdbcParam) (private in code, but 使用逻辑说明)
- public static JdbcSession buildSession(JdbcParam jdbcParam, JdbcOption option, String poolName)
  - 说明：基于 `JdbcParam` 初始化连接并创建 `JdbcSession`。若 `option.transaction == true` 会将连接 `setAutoCommit(false)`。
- public static JdbcSession buildSession(JdbcParam jdbcParam, JdbcOption option)
- public static JdbcSession buildSession(JdbcParam jdbcParam)
- public static JdbcSession buildSession(JdbcOption option)
- public static JdbcSession buildSession()
- public static JdbcSessionPool buildSessionPool(JdbcParam jdbcParam, int poolSize)
- public static JdbcSessionPool buildSessionPool(int poolSize)
- public static JdbcSessionPool buildSessionPool(JdbcParam jdbcParam, JdbcOption option, int poolSize)
- public static JdbcParam getSystemJdbcParam()

语义要点：`buildSession` 系列用于直接获取 `JdbcSession`（需手动关闭）；`buildSessionPool` 创建连接池，池内会预先初始化若干会话。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionInterface.java`
接口方法（代理/实现需遵守）：
- void execute(String executeSql, Map<String, Object> sqlParams)
- int update(String updateSql, Map<String, Object> sqlParams)
- <T> T queryOne(String querySql, Map<String, Object> sqlParams, Class<T> clazz)
- List<Map<String, Object>> query(String querySql, Map<String, Object> sqlParams)
- <T> List<T> query(String querySql, Map<String, Object> sqlParams, Class<T> clazz)
- int queryCount(String querySql, Map<String, Object> sqlParams)
- List<Map<String, Object>> insert(String tableName, Map<String, Object> resource, InsertOptions insertOptions)
- List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions)
- List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions)
- List<Map<String, Object>> insert(Object resource, InsertOptions insertOptions)
- List<Map<String, Object>> insertSql(String insertSql, Map<String, Object> sqlParams)
- int update(List<UpdateCondition> updateConditions)
- <T> int update(UpdateCondition<T> updateCondition)
- void createTable(Object resource)

备注：接口中提供了多种重载以便插入多种资源形态（表名+map、表名+list、对象自动生成 SQL 等）。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSession.java`
继承 `BaseCloseConnectJdbcSession` 并实现 `JdbcSessionInterface`，常用方法（均在调用后自动 close()，见源码实现）：
- public void execute(String sql, Map<String, Object> sqlParams)
- public int update(String updateSql, Map<String, Object> sqlParams)
- public <T> T queryOne(String querySql, Map<String, Object> sqlParams, Class<T> clazz)
- public List<Map<String, Object>> query(String querySql, Map<String, Object> sqlParams)
- public <T> List<T> query(String querySql, Map<String, Object> sqlParams, Class<T> clazz)
- public int queryCount(String querySql, Map<String, Object> sqlParams)
- public List<Map<String, Object>> insertSql(String insertSql, Map<String, Object> sqlParams)
- public List<Map<String, Object>> insert(String tableName, Map<String, Object> resource)
- public List<Map<String, Object>> insert(String tableName, Map<String, Object> resource, InsertOptions insertOptions)
- public List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources)
- public List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions)
- public List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources)
- public List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions)
- public List<Map<String, Object>> insert(Object resource)
- public List<Map<String, Object>> insert(Object resource, InsertOptions insertOptions)
- public void createTable(Object resource)
- public <T> int update(UpdateCondition<T> resource)
- public int update(List<UpdateCondition> resources)
- public <T> T executeBatch(Function<JdbcSessionProxy, T> handler)
- public void executeBatch(Consumer<JdbcSessionProxy> handler)
- public List<Table> getERStructure()
- public String getPoolName()
- public LocalDateTime getCreateTime()

语义要点：`JdbcSession` 的大多数方法在内部会调用对应的 `*UnClose` 版本并在 finally 中调用 `close()`，因此这些方法适用于单次短生命周期调用（自动关闭）。对需要长事务控制或批量操作且不希望立即关闭会话，请使用 `BaseTransactionJdbcSession` 或 `executeBatchUnCommit` / 相应 UnCommit 方法。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/BaseTransactionJdbcSession.java`
重要方法与语义：
- protected boolean hasUnCommit()
- public String getSessionId()
- public boolean isActive()
- public void commit()
- public void rollback()
- public int updateUnCommit(String updateSql, Map<String,Object> sqlParams)
- public List<Map<String,Object>> queryUnCommit(String querySql, Map<String,Object> sqlParams)
- public void executeUnCommit(String sql, Map<String,Object> sqlParams)
- public <T> T executeBatchUnCommit(Function<JdbcSessionProxy, T> handler)
- public void executeBatchUnCommit(Consumer<JdbcSessionProxy> handler)
- public List<Map<String,Object>> insertUnCommit(...)
- public void createTableUnCommit(Object resources)
- public List<Map<String,Object>> insertSqlUnCommit(String insertSql, Map<String,Object> sqlParams)
- public int updateUnCommit(List<UpdateCondition> resources)
- protected void closeFreeSession(long now)
- public void close()

语义要点：用于手动控制事务，方法名中带 `UnCommit` 表示操作不会在返回后自动提交或关闭。必须在操作完成后显式调用 `commit()` 或在异常时 `rollback()`。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionPool.java`
主要方法：
- JdbcSessionPool(JdbcParam jdbcParam, JdbcOption option, int poolSize) (构造器)
- public JdbcSession getSession()
  - 说明：从阻塞队列获取 `JdbcSession`，若为空会尝试新建并 `offer` 到队列。
- public void releaseSession(JdbcSession session)
- public <R> R useSession(Function<JdbcSession, R> sessionConsumer)
- public void useSession(Consumer<JdbcSession> sessionConsumer)
- public void shutdown()
- public String getPoolId()
- public LocalDateTime getActiveAt(), getCreateAt()
- public int getCurrentPoolSize()
- public DBType getType()
- public String getDatabase()

语义要点：`useSession` 会自动 release；`getSession()` 需要手动 `releaseSession()`。池并不建议用于承载长事务（源码中对 `unCommit()` 会在 `useSession` 中自动 commit 并警告）。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/JdbcParam.java`
字段与方法（要点）：
- 字段：`String url, String host, Integer port, String database, DBType dbType, String username, String password`
- public String getUrl()
  - 若 `url` 非空直接返回；否则尝试使用 `host/port/database` 与 `dbType.getUrlTemplate()` 生成 URL；若参数不足会抛出 `BaseException`。
- static factory helpers：`create`, `postgres`, `mysql` 等（便捷构造器）。
- Json support: `public JdbcParam(JsonObject jsonObject)` 使用 `JdbcParamConverter.fromJson`。`toJson()` 使用 `JdbcParamConverter.toJson`。

语义要点：若只设置 host/database/username/password 而未设置 port/dbType，会使用 `DBType.POSTGRESQL` 作为默认类型与默认端口（见 `getDbType()`）。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/JdbcOption.java`
字段与默认值：
- boolean transaction (默认 true)
- boolean log (默认 true)
- String pool (默认 null)
- ResultConverter converter (默认 `DefaultConverter`)
- boolean filterNull (默认 false)
- boolean test (默认 false)

语义要点：控制执行行为（是否启用事务、日志打印、结果转换与空值过滤等）。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/DBType.java`
枚举值（当前实现支持）：
- MYSQL, ORACLE, POSTGRESQL, TDGENINE
字段包括 JDBC 驱动类名，默认端口，URL 模板与测试 SQL。


### `step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/UserTransaction.java`
字段：`programCode, lockIp, oldDataKey, dataKey, editType, account, userCode, lockSqls`。
转换器：`UserTransactionConverter` 提供 `fromJson`/`toJson`。
语义要点：由上层传输参数 (`TransmitParam`) 构建，用于记录事务上下文/锁信息。


## 参数/选项/转换器详细说明

### `JdbcParam`（连接参数）
- url：完整 JDBC URL（优先）。
- host/port/database：可构造 url（依赖 `DBType` 的 `urlTemplate`）。
- dbType：数据库类型（若 null，返回 `DBType.POSTGRESQL`）。
- username/password：认证信息。

注意：若提供 `host/port/database`，但未提供 `dbType` 会抛异常；若只需系统配置可使用 `JdbcSessionBuilder.getSystemJdbcParam()`。


### `JdbcOption`（执行选项）
- transaction：若 true（默认），构建 `JdbcSession` 时会设置 `connection.setAutoCommit(false)`，需要手动 commit/rollback（或在池中由 `useSession` 自动 commit）。
- log：是否输出 SQL 日志。
- filterNull：查询结果是否过滤 null 值。
- converter：结果转换器（实现 `ResultConverter`），可替换默认转换逻辑。
- pool：期望绑定的池 id（可选）。
- test：用于内部测试/性能追踪的开关。


### 转换器
- `JdbcParamConverter` / `UserTransactionConverter`：由 Vert.x codegen 生成，主要用于 Json <-> 对象映射；AI 在序列化/反序列化时可安全使用这些转换器。
- `ResultConverter` / `DefaultConverter`：用于数据库结果到 Java 值的映射，可以通过 `JdbcOption.setConverter()` 注入自定义实现。


## 会话与事务生命周期模板

下面给出两套模板。注意：源码中 `JdbcSession` 会在大部分普通方法结束时自动 `close()`（因此适用于短生命周期单次调用）。若需要跨多步事务，请使用 `BaseTransactionJdbcSession` 或通过 `JdbcSessionBuilder`/`JdbcSessionPool` 构建并控制事务边界。

### 模板 A - try-with-resources（如果 `JdbcSession` 实现 `AutoCloseable`）

```java
// 检查 JdbcSession 是否实现 AutoCloseable；源码中没有明确声明 AutoCloseable，若实现可使用如下
JdbcParam param = JdbcParam.postgres("host","db","user","pass");
JdbcOption option = new JdbcOption();
option.setTransaction(false); // 简单执行无需手动 commit
try (JdbcSession session = JdbcSessionBuilder.buildSession(param, option)) {
    List<Map<String,Object>> rows = session.query("SELECT * FROM t WHERE id = #id#", Map.of("id", 1));
    // 处理 rows
}
// session 会自动 close()
```

说明：若 `JdbcSession` 未实现 `AutoCloseable`，请使用模板 B。


### 模板 B - try-catch-finally（显式 close / commit / rollback）

```java
JdbcParam param = JdbcParam.postgres("host","db","user","pass");
JdbcOption option = new JdbcOption();
option.setTransaction(true); // 希望手动控制事务
JdbcSession session = JdbcSessionBuilder.buildSession(param, option);
try {
    List<Map<String,Object>> rows = session.queryUnClose("SELECT * FROM t WHERE id = #id#", Map.of("id", 1));
    // 多步更新
    session.updateUnClose("UPDATE t SET col = #val# WHERE id = #id#", Map.of("val", "v", "id", 1));
    session.commit();
} catch (Exception ex) {
    session.rollback();
    throw ex; // 或记录错误并恢复
} finally {
    session.close();
}
```

说明：上述 `*UnClose` 方法是 `BaseTransactionJdbcSession` 提供的不自动关闭版本（源码中命名如 `insertUnCommit` / `updateUnCommit` / `queryUnCommit`），请根据实际运行时对象类型调用。


## 五个 AI 可直接调用的 Java 示例模板（含输入/输出/异常说明）

> 注意：以下均为模板示例。AI 在构建 SQL 时必须使用占位符 `#key#` 并通过 Map 提供参数，避免直接拼接未过滤字符串。

1) 初始化与获取会话（短生命周期）

- 输入：JdbcParam (host/db/user/pass)，JdbcOption
- 输出：短生命周期 `JdbcSession`，多数单次方法会自动 close

示例：
```java
JdbcParam param = JdbcParam.postgres("db-host","mydb","user","pass");
JdbcOption option = new JdbcOption();
try (JdbcSession session = JdbcSessionBuilder.buildSession(param, option)) {
    List<Map<String,Object>> rows = session.query("SELECT * FROM person WHERE id = #id#", Map.of("id", 123));
}
```
异常：若连接失败会抛 `BaseException`（封装 SQLException）。


2) 参数化查询（防注入）

- 输入：sql 带 `#key#` 占位，Map 参数
- 输出：List<Map<String,Object>> 或 转成 bean

示例：
```java
JdbcSession s = JdbcSessionBuilder.buildSession(param, new JdbcOption());
try {
    List<Map<String,Object>> data = s.query("SELECT * FROM account WHERE name = #name#", Map.of("name", "O'Reilly"));
    List<Account> list = s.query("SELECT * FROM account WHERE id > #minId#", Map.of("minId", 0), Account.class);
} finally {
    s.close();
}
```
说明：字符串参数在 `formatSqlParam` 中会自动做 `replace("'","''")` 转义。对于复杂或二进制内容请用其他机制。


3) 批量插入（通过 insert(table, List<Map>)）

- 输入：表名、List<Map<String,Object>>、InsertOptions（可选）
- 输出：List<Map<String,Object>>（若 INSERT 执行并返回 `RETURNING *` 则返回插入行）

示例：
```java
List<Map<String,Object>> rows = List.of(
    Map.of("name","a","age", 10),
    Map.of("name","b","age", 20)
);
JdbcSession session = JdbcSessionBuilder.buildSession(param);
try {
    List<Map<String,Object>> result = session.insert("person", rows);
} finally {
    session.close();
}
```
异常/边界：若 `sqlBuilder.insert` 返回 null 表示无法生成 SQL（例如没有有效列），返回空列表。


4) 事务示例（显式 begin/commit/rollback）

- 输入：需要跨多步的写操作
- 输出：操作结果整合

示例：
```java
JdbcOption option = new JdbcOption();
option.setTransaction(true);
BaseTransactionJdbcSession tx = (BaseTransactionJdbcSession) JdbcSessionBuilder.buildSession(param, option);
try {
    tx.updateUnCommit("UPDATE t SET v = #v# WHERE id = #id#", Map.of("v", 1, "id", 10));
    tx.insertUnCommit("person", Map.of("name","x"));
    tx.commit();
} catch (Exception ex) {
    tx.rollback();
    throw ex;
} finally {
    tx.close();
}
```
说明：确保获取到的是支持 `UnCommit` 方法的会话（如 `BaseTransactionJdbcSession` 或其子类）。


5) 并发与连接池示例（使用 `JdbcSessionPool`）

- 输入：JdbcParam、池大小
- 输出：通过 `useSession` 自动归还/commit 的安全执行

示例：
```java
JdbcSessionPool pool = JdbcSessionBuilder.buildSessionPool(param, 5);
try {
    pool.useSession(session -> {
        session.update("UPDATE t SET x = #x# WHERE id = #id#", Map.of("x", 2, "id", 1));
    });
} finally {
    pool.shutdown();
}
```
备注：不要在 `useSession` 提交长事务（源码会在发现未提交时自动 commit 并 warn）。


## 两个验证/测试建议（JUnit 模板说明）

说明：以下为建议的最小集成测试模板，帮助在 CI 中验证文档约定。文档中仅提供模板，若需我可以继续生成测试文件。

1) 测试 1 — 连接与简单查询
- 目标：确认 `JdbcSessionBuilder.buildSession()` 能够用 `JdbcParam` 建立连接并执行 select。
- 前置条件：提供测试数据库（可用 Docker 或 CI 的测试 database），或使用内存数据库（视 DBType）。
- 步骤：构建 `JdbcParam` -> `JdbcSessionBuilder.buildSession(param, option)` -> `session.query("SELECT 1 as v")` -> assert result size 1 and v==1。
- 预期：不抛异常，返回正确行。

2) 测试 2 — 事务 commit/rollback 行为
- 目标：验证 `BaseTransactionJdbcSession` 的 `updateUnCommit` + `commit` 与 `rollback` 行为。
- 步骤：
  - 在测试表插入一条可回滚的数据
  - 开启 transaction 的会话，执行 `updateUnCommit` 改变数据但不 commit，rollback -> 验证数据库未改变；
  - 再次执行 update 并 commit -> 验证数据库变更成功。
- 预期：rollback 恢复，commit 生效。

建议：在 CI 中将这两个测试放入集成测试阶段，并提供环境变量或 `JdbcParam` 的系统配置参数以指向测试数据库。


## 参考源码清单（相对路径）

- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSession.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionBuilder.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionManager.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionPool.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/JdbcSessionInterface.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/BaseTransactionJdbcSession.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/BaseJdbcSession.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/session/pool/druid/DruidSession.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/JdbcParam.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/JdbcOption.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/DBType.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/UserTransaction.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/JdbcParamConverter.java
- step-jdbc/runtime/src/main/java/com/step/jdbc/runtime/param/UserTransactionConverter.java


## 附录：快速上手（AI 指南）

- 获取会话：优先使用 `JdbcSessionBuilder.buildSession(...)` 或连接池 `JdbcSessionBuilder.buildSessionPool(...)`。
- 执行查询：使用 `session.query(sql, params)`（sql 中使用 `#key#` 占位）。
- 更新/插入：使用 `session.update(...)` / `session.insert(...)`，注意 insert 会尝试返回 `RETURNING *` 的结果。
- 事务：若需事务，请设置 `JdbcOption.transaction = true` 并显式 `commit()` 或 `rollback()`；或使用 `BaseTransactionJdbcSession` 的 `*UnCommit` 系列方法并在完成后提交。
- 参数化注意：避免拼接用户输入到 SQL 中，使用 `#key#` + Map，字符串会在 `formatSqlParam` 中被转义。


---

文件已按照本规范生成。如需把测试文件或更严格的 JSON Schema（便于 AI 自动生成调用参数）一并加入，请回复我会继续生成相应资源。
