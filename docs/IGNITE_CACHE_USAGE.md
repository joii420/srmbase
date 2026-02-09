# Ignite 缓存调用使用说明与规范（供开发者与 AI 使用）

版本说明：基于仓库文件
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/AbstractIgniteCacheSupport.java`
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/IgniteCacheSupport.java`
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/ExpiryIgniteCacheSupport.java`
- `step-cache/runtime/src/main/java/com/step/cache/runtime/config/CacheConfiguration.java`

目的：这份文档旨在让开发者或 AI 能准确找到并按规范调用项目中的 Ignite 缓存支持类与方法，包含配置说明、逐方法 API 参考、常见调用范式、进阶注意事项与源码快速定位表。

---

快速检查清单
- [x] 概览与配置（如何通过 `quarkus.cache.*` 控制）
- [x] 关键类与方法签名列表（按文件/类分组，并标注源码位置）
- [x] 每个方法的输入/输出/并发语义/重试行为与异常说明
- [x] 至少 4 个调用规范样例（非代码块，分步骤说明）
- [x] 进阶与注意事项（ScanQuery 开销、过期策略、quarkus.cache.retry-times 等）
- [x] 文档已保存路径：`step-cache/runtime/docs/IGNITE_CACHE_USAGE.md`

---

快速查找（文件 -> 可用 symbol）
- `step-cache/runtime/src/main/java/com/step/cache/runtime/config/CacheConfiguration.java`
  - `CacheConfiguration.initCache(IgniteClient ignite)` — 生产 `CacheAPI` 实例
  - `CacheConfiguration.initIgniteClient()` — 生产 `IgniteClient` 客户端配置
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/AbstractIgniteCacheSupport.java`
  - 构造器：`AbstractIgniteCacheSupport(IgniteClient client, String cacheName)`
  - 构造器（带过期策略）：`AbstractIgniteCacheSupport(IgniteClient client, String cacheName, ExpirePolicy expirePolicy, Duration timeout)`
  - 主要方法（API 列表见下文）
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/IgniteCacheSupport.java`
  - `IgniteCacheSupport` — 直接实现 `CacheAPI` 的轻量封装（继承自 `AbstractIgniteCacheSupport`）
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/ExpiryIgniteCacheSupport.java`
  - `ExpiryIgniteCacheSupport` — 支持过期策略（实现 `ExpiryCacheAPI`）

若需引用源码：按文件路径打开对应文件并查找方法名（如 `get`, `setExpired`, `lockWithRetry`）。

---

1) 概览与配置

- 使用入口：`CacheConfiguration.initCache(IgniteClient ignite)` 会根据 `quarkus.cache.open` 配置返回实际的缓存实现：
  - 当 `quarkus.cache.open == "false"` 时，返回 `NoCacheSupport`（无缓存实现），否则返回 `new IgniteCacheSupport(ignite, cacheName)`。
  - 配置位置：`step-cache/runtime/src/main/java/com/step/cache/runtime/config/CacheConfiguration.java`

- Ignite 客户端创建：`CacheConfiguration.initIgniteClient()` 使用 `ClientConfiguration` 并调用 `Ignition.startClient(cfg)`，可通过以下 `quarkus.cache.*` 配置项调整行为（在 Quarkus 配置中设置）：
  - `quarkus.cache.name`（默认 `igniteCache`） — 缓存基础名称
  - `quarkus.cache.host.address`（默认 `127.0.0.1:10800`） — Ignite client 地址
  - `quarkus.cache.password` — Ignite 用户密码（可选）
  - `quarkus.cache.open`（默认 `true`） — 是否启用缓存

- JVM/运行时配置项影响：
  - `quarkus.cache.retry-times`（读取自 `SystemUtil.getValue("quarkus.cache.retry-times", "10")`）会被 `AbstractIgniteCacheSupport` 解析并用作方法内部的 `retryTimes` 值；默认值字符串为 `10`。

---

2) 核心类与方法总览（含源码定位）

类：`AbstractIgniteCacheSupport` — 文件：`step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/AbstractIgniteCacheSupport.java`
- 构造器：
  - `AbstractIgniteCacheSupport(IgniteClient client, String cacheName)` — 基本缓存实例（无过期策略）
  - `AbstractIgniteCacheSupport(IgniteClient client, String cacheName, ExpirePolicy expirePolicy, Duration timeout)` — 使用指定过期策略创建带过期行为的缓存实例（内部通过 `withExpiryPolicy`）
- 主要常量/字段（文档化的默认值）：
  - `DEFAULT_EXPIRE_TIME = 10` （默认过期时间/秒，类内用于默认情形）
  - `DEFAULT_RETRY_COUNT = 5`
  - `DEFAULT_WAIT_TIME = 500` （毫秒）
  - `retryTimes` — 从 `quarkus.cache.retry-times` 读取，默认 `10`（见构造器）

- 关键方法（按功能分组）—— 方法签名与简要用途：
  - 原子计数与管理
    - `boolean removeAtomicLong(String key)` — 删除/关闭命名的原子长整型；返回是否成功。（调用 `client.atomicLong(key, 1, true).close()`）
    - `long increment(String key, long start, long quantity, boolean init)` — 获取或创建 `ClientAtomicLong` 并 `addAndGet(quantity)`；若未创建且 `clientAtomicLong == null` 返回 `start`。
      - 注意：`client.atomicLong(key, start, init)` 用于初始化原子值
  - 基本 KV 操作
    - `Object get(Object key)`
    - `void set(Object key, Object val)`（等价 put）
    - `boolean exist(Object key)` — 检查 key 是否存在
    - `boolean exist(Set<? extends Object> keys)` — 批量存在检查
    - `Object getName()` — 返回 cache 名称
    - `Map<Object, Object> getAll(Set<? extends Object> keys)`
    - `void setAll(Map<? extends Object, ? extends Object> map)` — 批量 put
    - `boolean replace(Object key, Object oldVal, Object newVal)`
    - `boolean replace(Object key, Object val)`
    - `boolean delete(Object key)`
    - `boolean delete(Object key, Object oldVal)`
    - `void deleteAll(Set<? extends Object> keys)`
    - `void deleteAll()` — 删除缓存内所有条目
    - `Object getAndSet(Object key, Object val)` — getAndPut
    - `Object getAndDelete(Object key)` — getAndRemove
    - `Object getAndReplace(Object key, Object val)`
    - `boolean setIfAbsent(Object key, Object val)` — putIfAbsent
    - `Object getAndSetIfAbsent(Object key, Object val)` — getAndPutIfAbsent
    - `void clear()` — cache.clear()
    - `void clear(Object key)`
    - `void clearAll(Set<? extends Object> keys)`
  - 过期相关（操作会创建瞬时带 expiry 的 view 并写入）
    - `void setExpired(Object key, Object val, Long expiredSeconds)` — 默认 CREATED 策略
    - `void setExpired(Object key, Object val, Long expiredSeconds, ExpirePolicy policy)` — 可指定 CREATED/MODIFY/TOUCHED
    - `boolean lockExpired(Object key, Object val, long expired)` — 使用 CreatedExpiryPolicy 对 putIfAbsent
  - 锁相关
    - `boolean lock(Object key, Object val)` — 等价于 `setIfAbsent`
    - `boolean lockWithRetry(Object key, Object val)` — 使用默认重试参数
    - `boolean lockWithRetry(Object key, Object val, int retryCount)`
    - `boolean lockWithRetry(Object key, Object val, long waitTime)`
    - `boolean lockWithRetry(Object key, Object val, int retryCount, long waitTime)`
    - 以及带过期的 `lockExpiredWithRetry(...)` 系列方法
  - JSON 便利方法（使用 `JsonUtil.format`）
    - `void replaceJson(Object key, Object val)` — 将 val 序列化为 JSON 后 replace
    - `void setJsonExpired(Object key, Object val, Long expiredSeconds)`
    - `void setJsonExpired(Object key, Object val, Long expiredSeconds, ExpirePolicy expirePolicy)`
    - `void setJson(Object key, Object val)`
  - 条件查询与删除（基于 ScanQuery）
    - `Set<String> getKeysByCondition(Function<String, Boolean> condition)` — 遍历所有 key，filter 后返回 key 字符串集合
    - `Map<String, Object> getKVByCondition(Function<String, Boolean> condition)` — 遍历所有 entry，filter 后返回 map
    - `void deleteByCondition(Function<String, Boolean> condition)` — 批量删除满足条件的 key

实现类：
- `IgniteCacheSupport` — 文件：`step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/IgniteCacheSupport.java`
  - 继承自 `AbstractIgniteCacheSupport` 并实现 `CacheAPI`（不增加新方法）

- `ExpiryIgniteCacheSupport` — 文件：`step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/ExpiryIgniteCacheSupport.java`
  - 继承自 `AbstractIgniteCacheSupport` 并实现 `ExpiryCacheAPI`，构造器允许通过 `ExpirePolicy` 与 `Duration` 指定策略

---

3) 重试与异常行为（executeWithRetry 概述）

- 目标：`AbstractIgniteCacheSupport` 内部对绝大多数对 cache/client 的调用都包装在 `executeWithRetry(Supplier<T>)`。
- 逻辑要点（源码实现）：
  - 从构造器读取 `retryTimes`（使用 `SystemUtil.getValue("quarkus.cache.retry-times", "10")`）；若读取异常，回退为 `10`。
  - `executeWithRetry` 在出现异常时会：
    1. 记录 warn 日志（含异常）
    2. 如果异常可重试（isRetryable(e) 返回 true，当前实现检查异常 message 是否包含 "Channel is closed"），并且尚未超出重试次数，则等待 500ms（Thread.sleep(500)）后重试
    3. 若达到重试次数或异常不可重试，则抛出异常（不封装为特定缓存异常）。
  - 注意：若 Thread.sleep 被中断，会设置中断标记并抛出 `BaseException("Retry interrupted").record()`。

- 影响：调用者无需自己做重试逻辑（类中已经提供），但必须意识到：
  - 某些错误不会被重试（isRetryable 的判定仅基于 message 包含 `Channel is closed`）
  - 重试间隔固定为 500ms（执行期间的 sleep）
  - `retryTimes` 可通过 `quarkus.cache.retry-times` 在运行时控制

---

4) 方法详细说明（选取常用方法与注意点）

- get(Object key)
  - 返回：缓存中 key 对应的 value（Object），如无则返回 null。
  - 并发语义：非原子；若需读写一致性需在业务侧控制（或使用事务/分布式锁）。
  - 重试：受 `executeWithRetry` 包装；在可重试异常情况下会自动重试。
  - 源码定位：`AbstractIgniteCacheSupport.get`（见文件并按方法名搜索）

- set(Object key, Object val)
  - 行为：将 val 放入缓存（put）；覆盖旧值。
  - 注意：若想保证“不覆盖”，请使用 `setIfAbsent`。
  - JSON 场景：可使用 `setJson`（使用 `JsonUtil.format(val)`）来统一序列化策略。

- setExpired(Object key, Object val, Long expiredSeconds, ExpirePolicy policy)
  - 行为：基于 policy（CREATED/MODIFY/TOUCHED）创建一个带 expiry 的 cache view 并执行 put。
  - Duration 单位为秒（内部 new Duration(TimeUnit.SECONDS, expiredSeconds)）
  - 源码注意：TOUCHED 分支在源码中仅创建了 TouchedExpiryPolicy 对象，但实际 put 分支请核对实现（以源码行为为准）。

- lock(Object key, Object val) / lockWithRetry(...)
  - 语义：基于 putIfAbsent（setIfAbsent），可以用于实现分布式锁（value 可为锁持有者 ID + 过期时间等）。
  - lockExpired 提供带过期的锁（通过 CreatedExpiryPolicy）
  - lockWithRetry 会循环尝试并在每次失败后 sleep `waitTime`（默认 500 毫秒），直到达到 retryCount

- increment(String key, long start, long quantity, boolean init)
  - 语义：基于 Ignite 的 `ClientAtomicLong`，会获取或创建命名原子长整型并增加值。
  - 返回最新值（addAndGet 返回值）；若 `clientAtomicLong == null` 返回 start。
  - 当作分布式计数器使用（注意：与缓存 key 空间分离，使用 client.atomicLong 命名空间）

- getKeysByCondition / getKVByCondition / deleteByCondition
  - 实现基于 `cache.query(new ScanQuery<>()).getAll()` 来读取全部 entry
  - 成本：这是全表扫描，适合小规模缓存或离线清理，不推荐频繁在大缓存上使用

---

5) 推荐的调用规范样例（非代码块，按步骤描述）

样例 A — 基本读取/写入
1. 通过 DI 或 `@Produces` 获取 `CacheAPI`（在默认配置下由 `CacheConfiguration.initCache` 提供）
2. 写入：调用 `set(key, value)`；若 value 为对象并需保持 JSON 格式一致性，先调用 `setJson(key, value)` 或在写入前用统一序列化
3. 读取：调用 `get(key)`，检查返回是否为 `null`
4. 更新策略：若只在 key 不存在时写入，使用 `setIfAbsent(key, value)`；若需读-改-写原子操作，考虑使用 `getAndSet` 或应用层分布式锁

样例 B — 写入带过期时间
1. 若需要单次写入并生效过期，调用 `setExpired(key, value, expiredSeconds)`（默认 CREATED 策略）
2. 若需要不同的过期语义，调用 `setExpired(key, value, expiredSeconds, ExpirePolicy.MODIFY)` 等
3. 若需对整个 cache（或 key）做长期过期策略，请考虑用 `ExpiryIgniteCacheSupport` 构造器来创建带 expiry 的 cache 实例（构造器需要 `ExpirePolicy` + `Duration`）

样例 C — 分布式锁（简单版）
1. 选定唯一 key，如 `lock:resource:123`，value 可为 `instanceId:timestamp`
2. 尝试调用 `lock(key, value)`（等同 `setIfAbsent`）—— 成功则获得锁
3. 释放锁：显式删除 key（`delete(key)`）或在锁创建时使用 `lockExpired` 来设置自动过期

样例 D — 分布式锁（带重试）
1. 选定唯一 key
2. 调用 `lockWithRetry(key, value, retryCount, waitTime)`；该方法会在失败时循环尝试并 sleep `waitTime`（ms）
3. 获得锁后处理业务，最后 `delete(key)` 释放；若使用 `lockExpiredWithRetry` 可自动设置过期以防死锁

样例 E — 原子计数器
1. 使用 `increment(counterKey, startValue, delta, init)` 来增加计数，`init=true` 表示如果不存在则创建并设置初始值
2. 若要删除计数器资源，调用 `removeAtomicLong(counterKey)`（会调用 close）

命名与序列化约定（建议）
- key 命名建议使用有前缀的命名空间风格：`{app}:{domain}:{type}:{id}`，例如 `order:payment:lock:98765`
- 对复杂对象统一 JSON 序列化：使用 `setJson` / `replaceJson` / `setJsonExpired`，保持读写端约定一致
- 锁 key 的 value 建议包含持有者标识与时间戳，以便诊断与强制释放

---

6) 进阶与注意事项

- ScanQuery 成本：`getKeysByCondition`, `getKVByCondition`, `deleteByCondition` 都会触发 `cache.query(new ScanQuery<>()).getAll()`，这是全量扫描操作。尽量避免在大规模缓存或高并发生产流量上频繁使用。可用外部索引/单独记录 key 集合的方式替代。

- deleteAll / clear
  - `deleteAll()` 会调用 `cache.removeAll()`，删除缓存内所有条目；`clear()` 会调用 `cache.clear()`。
  - 注意差异与持久化后端的影响：这些操作是破坏性的，可能影响其他业务，请谨慎使用并加上安全校验。

- 过期策略差异（`ExpirePolicy`）
  - CREATED（CreatedExpiryPolicy）：从创建时间开始计时，通常用于写入后固定过期
  - MODIFY（ModifiedExpiryPolicy）：在每次修改（put）时重置到新的到期时间
  - TOUCHED（TouchedExpiryPolicy）：在每次访问时（读取/写入）更新过期时间（注意：具体行为可参考 JCache 规范与 Ignite 文档）
  - `ExpiryIgniteCacheSupport` 的构造器可以提前将 cache 绑定到某种策略，适用于大多数键都共用相同策略的场景

- 重试策略（配置点）
  - `quarkus.cache.retry-times`（System property / environment config）控制 `retryTimes`。
  - `executeWithRetry` 只会对 message 中包含 `"Channel is closed"` 的异常判定为 retryable；如需更灵活的重试策略，需要在调用层或扩展类中实现。

- 日志与异常
  - 操作失败时，异常会被上抛（包装或原样抛出），并在每次失败时写入 warn 级别日志（`log.warn(e.getMessage(), e);`）。调用方应捕获并记录或转换为业务友好的错误码。

---

7) AI / 自动化 使用建议（方便被模型或工具调用）
- 当 AI 需要定位实现或构造器时，优先使用文件路径 + 方法名（例如 `AbstractIgniteCacheSupport.get(Object)`），不要依赖行号，因为行号可能随代码变动。路径已在本文件顶部列出。
- 若自动生成调用代码：
  - 按需注入 `IgniteClient`（由 `CacheConfiguration.initIgniteClient` 生产），再用 `new IgniteCacheSupport(ignite, cacheName)` 或通过容器注入 `CacheAPI`。
  - 若需要带过期策略的 cache，请调用 `new ExpiryIgniteCacheSupport(ignite, cacheName, ExpirePolicy.CREATED, new Duration(TimeUnit.SECONDS, 30))`（在实际代码中使用 `Duration` 类并传入合适参数）。
- 配置环境：测试环境上可将 `quarkus.cache.open=false` 来禁用缓存（使 `initCache` 返回 `NoCacheSupport`），便于单元/集成测试。

---

8) 参考源码位置（便于复制到 AI 提示或代码检索）
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/AbstractIgniteCacheSupport.java`
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/IgniteCacheSupport.java`
- `step-cache/runtime/src/main/java/com/step/cache/runtime/ignite/support/ExpiryIgniteCacheSupport.java`
- `step-cache/runtime/src/main/java/com/step/cache/runtime/config/CacheConfiguration.java`

---

附录：快速查找示例（AI 可按此构造检索语句）
- 若需要 `lockWithRetry` 的实现片段：打开 `AbstractIgniteCacheSupport.java`，搜索 `lockWithRetry(Object key, Object val, int retryCount, long waitTime)`。
- 若需要原子计数器：搜索 `increment(String key, long start, long quantity, boolean init)`。
- 若需要 JSON 便捷方法：搜索 `setJson` / `replaceJson`。

---

文档维护建议
- 将本文件纳入代码仓库的 docs 目录，并在后续更改 `AbstractIgniteCacheSupport` 时同步更新本文件中的方法签名与语义描述。
- 如需为 AI 自动生成调用示例代码，可考虑在 `docs/` 下另存 `IGNITE_CACHE_EXAMPLES.java`（示例代码）并维护测试以保证示例与实现 API 同步。


EOF
