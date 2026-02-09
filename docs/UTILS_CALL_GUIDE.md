# 工具包调用文档（step-tool/utils）

本文档整理自 `step-tool/src/main/java/com/step/tool/utils` 下的工具类，目标是为开发者或自动化 agent 提供可直接调用的 API 概览、注意事项与简明示例（Java 片段）。

---

## 目录

- 概述与调用规范
- 依赖清单（常见第三方库）
- 类列表与 API 概览（按文件）
  - `BeanUtil`
  - `ByteUtil`
  - `CollectionUtil`
  - `ConcurrentDateFormat`
  - `DateUtil`
  - `ExcelUtil`
  - `Func`
  - `HttpUtil`
  - `JoiiPrint`
  - `JoiiUtil`
  - `JsonUtil`
  - `ListUtil`
  - `Maps`
  - `MathUtil`
  - `PathUtil`
  - `RequestUtil`
  - `RoutingContextUtil`
  - `StringPool`
  - `StringUtil`
  - `SystemUtil`
- 常用调用样例（可复制片段）

---

## 概述与调用规范（总览）

1. 语言与位置
   - 这些工具类位于 `com.step.tool.utils` 包，源文件在 `step-tool/src/main/java/com/step/tool/utils`。

2. 异常模型
   - 项目内部常用 `com.step.api.runtime.exception.base.BaseException` 来封装运行时错误。很多工具在遇到不可恢复错误时会抛出或包装成 `BaseException`。调用方可按需捕获并处理。

3. 空值与返回约定
   - 许多工具在输入为 `null` 或空时会返回 `null` 或空集合/数组；部分工具（如 `RequestUtil.sendPost`）在 HTTP 返回码非 200 时会抛出 `BaseException`。
   - 请在调用前对输入做必要的空检查以避免 NPE（若想依赖工具类的空处理行为，也可直接调用）。

4. 线程与并发
   - `ConcurrentDateFormat` 是为并发设计的日期格式化器（内部使用队列维护 `SimpleDateFormat` 实例）。`DateUtil` 中的常量 `DATETIME_FORMAT` 等均使用该类，适合多线程调用。

5. 日志与调试
   - 工具类内部使用 `slf4j` 或 `System.out` 输出；请在生产环境用合适的 logger 等级收集日志。

6. 推荐风格
   - 尽量使用 `JsonUtil.format` / `JsonUtil.parse` 做对象与 JSON 的序列化/反序列化。
   - 对于 HTTP 操作优先使用 `RequestUtil`（封装了标准 Java HttpURLConnection），文件上传可使用 `RequestUtil.sendFile` 或 `HttpUtil.sendFile`（基于 Apache HttpClient）。

---

## 依赖清单（建议在模块 pom 中引入）

- cn.hutool: hutool-core
- com.alibaba: fastjson
- com.fasterxml.jackson.core: jackson-databind
- com.fasterxml.jackson.datatype: jackson-datatype-jsr310
- org.apache.httpcomponents: httpclient
- org.apache.poi: poi, poi-ooxml
- org.slf4j: slf4j-api, 实现（logback 或 log4j）

示例（pom 依赖片段）：

```xml
<!-- 说明：项目已有依赖时无需重复 -->
<dependencies>
  <dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-core</artifactId>
    <version>5.8.0</version>
  </dependency>
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.83</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.14.0</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.14.0</version>
  </dependency>
  <dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
  </dependency>
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
  </dependency>
</dependencies>
```

---

## 类列表与 API 概览（逐类）

注：方法签名基于源码的可见 public 方法；示例会尽量使用最小依赖并可直接复制。

### 1) `BeanUtil` (extends hutool)

用途：Bean 复制、Map->Bean、实例化等。

主要方法：

- `public static void copy(Object source, Object target)`
  - 作用：将 `source` 的属性拷贝到 `target`（使用 Hutool 的 `copyProperties`）。

- `public static <T> T copy(Object source, Class<T> tClass)`
  - 作用：将 `source` 拷贝并返回目标类型实例。

- `public static <T> T mapToBean(Map<String, Object> map, Class<T> tClass)`
  - 作用：从 Map 填充 bean（忽略 null、忽略大小写等，使用 CopyOptions）。

- `public static <T> T newInstance(Class<?> clazz)`
  - 作用：通过 `clazz.newInstance()` 创建实例，失败时抛 `BaseException`。

- `public static int random(int start, int end)`
  - 作用：返回 [start, end) 之间的随机 int。

- `public static <T> T getParam(TransmitParam dto, Class<T> obj)`
  - 作用：从 `TransmitParam.getParam()` 的 Map 转换为目标对象（通过 Vert.x JsonObject）。

示例（拷贝）：

```java
MyDto source = ...;
MyVo target = new MyVo();
BeanUtil.copy(source, target);

Map<String,Object> map = Map.of("name","zhangsan","age",18);
User u = BeanUtil.mapToBean(map, User.class);
```

---

### 2) `ByteUtil`

用途：字节与十六进制/字符串之间互转、字节数组拼接。

方法：
- `public static String byteToHex(byte[] src)`
- `public static String byteToStr(byte[] src)`
- `public static byte[] hexToByte(String src)`
- `public static byte[] byteAdd(byte[] a, byte[] b)`

示例：

```java
byte[] data = {8, 18};
String hex = ByteUtil.byteToHex(data); // -> "0812"
byte[] back = ByteUtil.hexToByte(hex);
byte[] merged = ByteUtil.byteAdd(data, back);
```

---

### 3) `CollectionUtil`

用途：类继承自 Hutool 的 `CollectionUtil`，直接使用 Hutool 的方法。

（无新增方法）

---

### 4) `ConcurrentDateFormat`

用途：线程安全的 `SimpleDateFormat` 池，适合高并发场景。

构造/工厂：
- `public static ConcurrentDateFormat of(String format)`
- `public static ConcurrentDateFormat of(String format, TimeZone timezone)`
- `public static ConcurrentDateFormat of(String format, Locale locale, TimeZone timezone)`

功能：
- `public String format(Date date)`
- `public Date parse(String source) throws ParseException`

示例：

```java
String s = ConcurrentDateFormat.of("yyyy-MM-dd HH:mm:ss").format(new Date());
Date d = ConcurrentDateFormat.of("yyyy-MM-dd").parse("2024-07-01");
```

线程注意：本类内部维护 `ConcurrentLinkedQueue<SimpleDateFormat>`，适合多线程共用。

---

### 5) `DateUtil`

用途：常用日期/时间工具，封装 Java8 时间与老 Date 类型的互转、格式化、加减等。

常量：
- `PATTERN_DATETIME`, `PATTERN_DATE`, `DATETIME_FORMAT` (ConcurrentDateFormat), `DATE_FORMAT` 等

常用方法（部分）：
- `public static Date now()`
- `public static String formatDateTime(Date date)`
- `public static String formatDate(Date date)`
- `public static Date parse(String dateStr, String pattern)`
- `public static LocalDateTime parseLocalDateTime(String timeStr)`
- `public static Date plusDays(Date date, long daysToAdd)`（同类的 plusWeeks/plusMonths/...）
- `public static Duration between(Date startDate, Date endDate)`
- `public static String secondToTime(Long second)`
- `public static String today()`

示例：

```java
String nowStr = DateUtil.formatDateTime(new Date());
Date tomorrow = DateUtil.plusDays(new Date(), 1);
LocalDateTime ldt = DateUtil.parseLocalDateTime("2024-07-01 12:00:00");
```

注意：parse 系列在解析异常时会抛 `BaseException`（包装 ParseException）。

---

### 6) `ExcelUtil`

用途：基于 Apache POI 的导出/读取工具。

主要方法：
- `public static byte[] exportSheets(List<ExcelSheetExport> sheets)`
  - 作用：按传入的 `ExcelSheetExport` 列表生成 xlsx 的字节数组。
- `public static byte[] export(List<Map<String, Object>> dataList)`
  - 作用：快捷导出单 sheet（sheet 名为当前日期）的字节数组。
- `public static List<Map<String,Object>> read(String filePath)`
- `public static List<Map<String,Object>> read(String filePath, String sheetName)`
- `public static List<Map<String,Object>> read(String filePath, int sheetIndex)`

注意：`ExcelSheetExport`、`ExcelTitle` 来自 `com.step.model`，示例中可使用 `export(List<Map<...>>)`。

示例（导出并写入文件）：

```java
List<Map<String,Object>> rows = new ArrayList<>();
rows.add(Map.of("name", "zhangsan", "age", 18));
byte[] excel = ExcelUtil.export(rows);
Files.write(Paths.get("/tmp/users.xlsx"), excel);
```

读取示例：

```java
List<Map<String,Object>> data = ExcelUtil.read("/tmp/users.xlsx");
```

---

### 7) `Func`

用途：常用小工具（字符串拆分、数字转换、join 等）。

常用方法（部分）：
- `public static List<String> toStrList(String str)`
- `public static String[] toStrArray(String str)` / `toStrArray(String split, String str)`
- `public static boolean isEmpty(final CharSequence cs)` （委托到 `StringUtil.isEmpty`）
- `public static List<Long> toLongList(String str)`
- `public static int toInt(final Object str)` / `toInt(final Object str, final int defaultValue)`
- `public static Double toDouble(Object value, Double defaultValue)`
- `public static long toLong(final Object str, final long defaultValue)`
- `public static String toStr(Object str, String defaultValue)`
- `public static String join(Collection<?> coll, String delim)`
- `public static String[] split(String str, String delimiter)`

示例：

```java
int x = Func.toInt("123");
List<Long> ids = Func.toLongList("1,2,3");
String joined = Func.join(List.of(1,2,3)); // "1,2,3"
```

---

### 8) `HttpUtil`

用途：基于 Apache HttpClient 的文件上传（Multipart）简化方法。

方法：
- `public static <T> com.step.model.HttpResponse<T> sendFile(String path, File file, Map<String,String> formParams, Class<T> clazz)`
  - 作用：向指定 `path` 上传 `file`，同时可以提交 `formParams`；返回封装的 `HttpResponse<T>`。
  - 错误：若文件不存在或不是文件会返回错误 HttpResponse，也会在异常时返回 `HttpResponse.error(...)`。

示例：

```java
File f = new File("/tmp/upload.zip");
HttpResponse<String> resp = HttpUtil.sendFile("http://example.com/upload", f, Map.of("tenantIds","1,2"), String.class);
if (resp.isSuccess()) { System.out.println(resp.getData()); }
```

---

### 9) `JoiiPrint`

用途：控制台带颜色输出的辅助工具（ANSI 颜色码），包含快捷方法 `red/green/blue/...` 。

常用方法：
- `public static void red(String msg, Object... options)` 等颜色方法。

示例：

```java
JoiiPrint.red("错误消息");
JoiiPrint.green("成功");
```

注意：Windows 控制台可能需要支持 ANSI（或在 IDE 控制台中查看效果）。

---

### 10) `JoiiUtil`

用途：行级文本文件编辑，基于 Java IO + 函数式接口。

方法：
- `public static void editTxt(String sourceFilePath, String targetFilePath, Function<String,String> lineEdit)`
  - 作用：按行读取 `sourceFilePath`，对每行应用 `lineEdit`，写入 `targetFilePath`。

示例：

```java
JoiiUtil.editTxt("a.txt","b.txt", line -> line.replace("foo","bar"));
```

---

### 11) `JsonUtil`

用途：项目统一的 JSON（反）序列化工具，内部使用 Jackson 与 fastjson 混合策略，支持 Java 8 时间类型。

常量：
- `DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"`
- `DEFAULT_DATE_FORMAT = "yyyy-MM-dd"`
- `DEFAULT_TIME_FORMAT = "HH:mm:ss"`

主要方法：
- `public static String toString(Object obj)` — 使用 fastjson 的 `JSONObject.toJSONString`（若 obj==null 返回 null）。
- `public static String format(Object object)` — 使用 Jackson `ObjectMapper`，将对象格式化为 JSON 字符串（null 返回 null）；抛 `BaseException` 包装异常。
- `public static <T> T parse(String str)` — 泛型解析到目标类型（内部使用 Jackson TypeReference）。
- `public static JsonNode parseNode(Object obj)` / `parseNode(String str)` — 返回 Jackson `JsonNode`。
- `public static <T> T parse(String str, Class<T> beanClass)` — 使用 fastjson 将字符串解析为指定类型。
- `public static <T> List<T> parseList(List<Map<String,Object>> list, Class<T> clz)` — 将 List<Map> 转为 List<T>。

示例：

```java
String json = JsonUtil.format(Map.of("name","zhangsan"));
User u = JsonUtil.parse(json, User.class);
JsonNode node = JsonUtil.parseNode(json);
```

注意：`format` 使用了为 Java8 时间类型注册的 `JavaTimeModule`，可正确序列化 `LocalDate`/`LocalDateTime`。

---

### 12) `ListUtil`

用途：集合分组并批量执行（分片处理）。

方法：
- `public static <T,R> List<R> groupExecute(List<T> resource, int groupSize, Function<List<T>, List<R>> function)`
- `public static <T,R> List<R> groupExecute(List<T> resource, Function<List<T>, List<R>> function)`（默认 groupSize=50）

示例：

```java
List<Integer> all = IntStream.range(0, 102).boxed().collect(Collectors.toList());
List<Integer> out = ListUtil.groupExecute(all, 30, chunk -> chunk.stream().map(i -> i+100).collect(Collectors.toList()));
```

---

### 13) `Maps`

用途：Map 辅助封装（链式访问、必需字段获取、类型转换）。

用法要点：
- `public static Maps from(Map<String,Object> param)` — 包装已有 map。
- `public String mustGet(String key)` — 必须存在，否则抛 `BaseException`。
- `public int mustGetInt(String key)` / `public long mustGetLong(String key)` — 转换异常抛 `BaseException`。
- `public Maps mustGetParam(String key)` — 将嵌套 map 包装为 Maps。
- `public List<Maps> mustGetParamList(String key)` — 将 list<Map> 转为 List<Maps>，若空抛异常。
- `public String get(String key, String defaultValue)`
- `public Object getObject(String key)`
- `public Maps set(String k, Object v)` （链式）
- `public Map<String,Object> map()` — 返回原 map
- `public String formatSql(String sql)` — 将 `#key#` 占位替换为 `mustGet(key)` 返回值。
- `public static Maps init()` / `init(String k,Object v)`

示例：

```java
Maps m = Maps.init("userId", 1).set("name", "zhang");
String name = m.mustGet("name");
Map<String,Object> raw = m.map();
```

注意：`mustGet*` 系列会抛 `BaseException`，请在调用方捕获并处理。

---

### 14) `MathUtil`

用途：常用数值运算（主要是带格式的除法与保留两位等）。

方法：
- `public static String divide(long a, long b)` — 返回百分比（不保留小数）字符串，b==0 抛 `BaseException`。
- `public static Integer divideInt(long a, long b)` — 返回百分比整数。
- `public static double sacleTwo(double a)` — 保留两位小数（四舍五入规则为 BIGDECIMAL ROUND_HALF_DOWN）。

示例：

```java
String pct = MathUtil.divide(1, 3);
```

---

### 15) `PathUtil`

用途：拼接资源路径（基于项目启动目录）。

方法：
- `public static String getResourcePath(String path)` — 返回 `System.getProperty("user.dir") + "\\src\\main\\resources\\" + path` 并规范化路径分隔符为 `\\`。

示例：

```java
String p = PathUtil.getResourcePath("templates/config.yml");
```

---

### 16) `RequestUtil`

用途：封装的 HTTP 客户端工具（基于 `HttpURLConnection`），提供 GET/POST、文件上传、获取远端 IP 等。

重要方法（摘要）：

- GET 系列：
  - `public static String sendGet(String address, Map<String,Object> params)`
  - `public static Response sendGetResponse(String address, Map<String,Object> params)`
  - `public static <T> T sendGet(String address, Map<String,Object> params, Class<T> responseEntity)`
  - 支持 headers / cookies 等重载。返回 `Response`（模型在 `com.step.model.Response`）。

- POST 系列：
  - `public static String sendPost(String address, Map<String,Object> body)`
  - `public static String sendPost(String address, String body)`
  - `public static Response sendPostResponse(String address, Map<String,String> headers, Map<String,Object> body)`
  - 支持 headers/cookies 返回 `Response`。
  - 若返回码 != 200，会抛 `BaseException`（在 `sendPost` 的封装中）。

- 文件上传（HttpURLConnection Multipart）
  - `public static String sendFile(String filePath, String targetPath)`
    - 说明：以 multipart/form-data 方式把本地文件上传到 targetPath（通过写流手工实现）。在失败时抛 `BaseException`。

- `public static String getRequestIp(HttpServerRequest request)` — 从 Vert.x `HttpServerRequest` 获取客户端 IP。

内部实现说明：
- `send(...)` 方法负责进行连接、设置 Header、写入 body（POST）并读取响应，包装为 `Response`。

示例（POST JSON 并解析结果）：

```java
Map<String,Object> body = Map.of("name","zhang");
String resp = RequestUtil.sendPost("http://example.com/api/user", body);
Result r = JsonUtil.parse(resp, Result.class);
```

示例（文件上传）：

```java
String res = RequestUtil.sendFile("C:/tmp/file.txt", "http://example.com/upload");
```

注意：`send(...)` 的超时、错误处理比较基础（如需高性能或更复杂的认证/重试策略，建议使用 Apache HttpClient 或 OKHttp）。

---

### 17) `RoutingContextUtil`

用途：Vert.x RoutingContext 的简单查询字符串解析：

- `public static JsonObject getParams(RoutingContext context)`
  - 说明：从 `HttpServerRequest` 的 `request.query()` 中解析类似 `a=1&b=2` 的字符串为 `JsonObject`。

示例：

```java
JsonObject params = RoutingContextUtil.getParams(ctx);
String v = params.getString("a");
```

注意：实现假定 `query()` 存在且含 `&` 分隔符，解析较为基础，未做 URL decode，调用方如需更健壮解析请在外层处理。

---

### 18) `StringPool`

用途：常量池（字符串常量集合），便于统一使用。

示例：

```java
String s = StringPool.EMPTY;
```

---

### 19) `StringUtil`

用途：字符串与基本工具方法集（空判断、格式化、替换、下划线转换等）。

常用方法：
- `public static boolean isEmpty(Object... objs)` — 多参判断（null/空字符串/集合/Map）
- `public static boolean isNotEmpty(Object... objs)`
- `public static String isEnoughSubstr(String src, int len)`
- `public static boolean isContainChinese(String str)`
- `public static String stringPadding(String src, int len, int flag, String c)`
- `public static boolean isAnyNotEmpty(Object... objs)`
- `public static String format(String message, Object... arguments)` — 类似占位替换 `{}` -> 参数
- `public static String cleanIdentifier(String param)` — 过滤为 Java 标识符字符
- `public static String convertStrToSnakeCase(String input)` — 驼峰转下划线小写

示例：

```java
String t = StringUtil.format("my name is {}, age {}", "L.cm", 30);
boolean empty = StringUtil.isEmpty("", null);
String snake = StringUtil.convertStrToSnakeCase("myFieldName"); // "my_field_name"
```

---

### 20) `SystemUtil`

用途：系统级配置信息读取（读取 `application.properties` 与 profile 文件），判断操作系统，获取应用运行时间等。

要点：
- 静态初始化会尝试加载 `application.properties`（classpath 根）与 `application-{profile}.properties`（基于 `quarkus.profile` 系统属性）。
- `public static String getValue(String key)` / `getValue(String key, String defaultValue)`
- `public static boolean isLinux()`
- `public static long systemStartSeconds()`

示例：

```java
String val = SystemUtil.getValue("my.config.key", "default");
boolean linux = SystemUtil.isLinux();
```

注意：`getValue` 支持占位符解析 `${var}`，并会做递归解析，若出现循环引用会抛 `IllegalStateException`。

---

## 常用调用样例（便于 AI/工程化调用）

下面的示例尽量写成可以直接复制到 Java 方法中的片段，必要时补上 imports。

1) JSON 序列化与反序列化

```java
// 依赖: JsonUtil
MyDto dto = new MyDto();
String json = JsonUtil.format(dto);
MyDto parsed = JsonUtil.parse(json, MyDto.class);
```

2) HTTP POST（JSON body）并解析响应

```java
Map<String,Object> body = new HashMap<>();
body.put("name", "alice");
String respJson = RequestUtil.sendPost("http://127.0.0.1:8080/api/user", body);
UserResp resp = JsonUtil.parse(respJson, UserResp.class);
```

3) 文件上传（简单）

```java
// 使用 RequestUtil
String response = RequestUtil.sendFile("C:/tmp/report.pdf", "http://server/upload");

// 或者使用 HttpUtil（需要 apache httpclient 依赖）
File file = new File("C:/tmp/report.pdf");
HttpResponse<String> r = HttpUtil.sendFile("http://server/upload", file, Map.of("tenantIds","1"), String.class);
```

4) Excel 导出（简易）

```java
List<Map<String,Object>> rows = new ArrayList<>();
rows.add(Map.of("name","张三","age",30));
byte[] excelBytes = ExcelUtil.export(rows);
Files.write(Paths.get("users.xlsx"), excelBytes);
```

5) 日期格式化

```java
String now = DateUtil.formatDateTime(new Date());
LocalDateTime ldt = DateUtil.parseLocalDateTime("2024-07-01 12:00:00");
```

6) 分批处理（ListUtil）

```java
List<Integer> all = IntStream.range(0,1000).boxed().collect(Collectors.toList());
List<Integer> processed = ListUtil.groupExecute(all, 100, chunk -> {
    return chunk.stream().map(i -> i * 2).collect(Collectors.toList());
});
```

7) 使用 Maps 访问参数

```java
Maps m = Maps.from(Map.of("id", 123, "name", "zhang"));
int id = m.mustGetInt("id");
String name = m.get("name", "default");
```

8) 读取配置与占位符解析

```java
String url = SystemUtil.getValue("service.url", "http://localhost:8080");
```

9) 控制台彩色输出

```java
JoiiPrint.red("出错啦：xxx");
JoiiPrint.green("完成");
```

---

## 常见注意事项与建议

- 对外部网络调用（`RequestUtil` / `HttpUtil`）建议在调用端实现超时重试、熔断等策略。当前工具实现为简单 HttpURLConnection 或 HttpClient 使用，请根据生产要求增强。
- `RoutingContextUtil.getParams` 中对 query 的解析未做 URL decoding，如需支持编码请在调用处调用 `URLDecoder.decode`。
- `ExcelUtil` 使用 Apache POI，输出为 `byte[]`，需注意在大数据量时的内存占用（考虑流式写入或分页导出）。
- `JsonUtil.format` 在序列化 Java8 时间类型时已做注册，建议项目中统一使用该方法以免时间序列化不一致。
- `SystemUtil` 在静态初始化时加载 `application.properties`，如果运行环境中没有该文件会记录警告。

---

## 文档位置

该文档已生成并存放于项目根目录下：

`docs/UTILS_CALL_GUIDE.md`

如需更详细的「每个方法逐行参数说明 + 返回值示例」版本，我可以在此基础上继续扩展为更大的参考手册（或生成单独的快速参考页）。

---

更新时间: 2026-02-09


