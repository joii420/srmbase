package com.step.jdbc.runtime.session;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.session.converter.DefaultConverter;
import com.step.jdbc.runtime.session.converter.ResultConverter;
import com.step.jdbc.runtime.session.options.InsertOptions;
import com.step.jdbc.runtime.session.support.JdbcSqlBuilder;
import com.step.jdbc.runtime.session.support.UpdateCondition;
import com.step.jdbc.runtime.txedit.exception.JdbcError;
import com.step.logger.LOGGER;
import com.step.tool.utils.DateUtil;
import com.step.tool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Sun
 * @date : 2023/7/4  12:58
 */
public abstract class BaseJdbcSession {

    public static final Logger log = LoggerFactory.getLogger(BaseJdbcSession.class);
    private final ResultConverter defaultConverter = new DefaultConverter();
    private final JdbcSqlBuilder sqlBuilder;

    private final JdbcOption option;

    private Set<String> createdTableSet = new HashSet<>();

    protected BaseJdbcSession(JdbcSqlBuilder sqlBuilder, JdbcOption option) {
        this.sqlBuilder = sqlBuilder;
        this.option = option;
    }

    protected List<Map<String, Object>> resultSetToMap(JdbcOption option, ResultSet resultSet) throws SQLException {
        boolean filterNull = option.getFilterNull();
        ResultConverter converter = Optional.ofNullable(option.getConverter()).orElse(defaultConverter);
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        //遍历加入
        while (resultSet.next()) {
            Map<String, Object> maps = new TreeMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                Object value = resultSet.getObject(i);
                final String typeName = "";
                if (filterNull) {
                    if (value != null) {
                        maps.put(columnLabel, converter.convert(typeName, value));
                    }
                } else {
                    maps.put(columnLabel, converter.convert(typeName, value));
                }
            }
            result.add(maps);
        }
        return result;
    }

    protected int queryCount(String sessionId, Statement statement, JdbcOption option, String querySql) {
        return queryCount(sessionId, statement, option, querySql, null);
    }

    protected int queryCount(String sessionId, Statement statement, JdbcOption option, String querySql, Map<String, Object> sqlParams) {
        if (StringUtil.isEmpty(querySql) || !querySql.trim().toUpperCase().startsWith("SELECT")) {
            throw new BaseException(JdbcError.CON_005.getMessage()).record();
        }
        this.printSql(sessionId, option, querySql);
        querySql = formatSqlParam(querySql, sqlParams);
        ResultSet resultSet = null;
        long start = System.currentTimeMillis();
        try {
            resultSet = statement.executeQuery(querySql);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (option.getLog()) {
                    LOGGER.info(log, "SESSION: %s -------> 查询数量: %s  %n", sessionId, count);
                }
                return count;
            }
            return 0;
        } catch (SQLException e) {
            printError(e, sessionId, querySql);
            throw new BaseException(e);
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > 1000) {
                log.warn("警告: {} SQL执行时间: {} | {} ", sessionId, time, querySql);
            } else if (option.getLog()) {
                log.info("{} SQL执行时间: {} ", sessionId, time);
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.error(log, e, "关闭resultSet失败: ");
                }
            }
        }
    }

    protected int update(String sessionId, Statement statement, JdbcOption option, String updateSql) {
        return update(sessionId, statement, option, updateSql, null);
    }

    protected int update(String sessionId, Statement statement, JdbcOption option, String updateSql, Map<String, Object> sqlParams) {
        if (StringUtil.isEmpty(updateSql) || updateSql.trim().toUpperCase().startsWith("SELECT")) {
            throw new BaseException(JdbcError.CON_005.getMessage()).record();
        }
        updateSql = formatSqlParam(updateSql, sqlParams);
        this.printSql(sessionId, option, updateSql);
        long start = System.currentTimeMillis();
        try {
            int i = statement.executeUpdate(updateSql);
            if (option.getLog()) {
                LOGGER.info(log, "SESSION: %s -------> 影响行数: %s 行结果 %n", sessionId, i);
            }
            return i;
        } catch (SQLException e) {
            printError(e, sessionId, updateSql);
            throw new BaseException(e);
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > 1000) {
                log.warn("警告: {} SQL执行时间: {} | {} ", sessionId, time, updateSql);
            } else if (option.getLog()) {
                log.info("{} SQL执行时间: {} ", sessionId, time);
            }
        }
    }

    protected String formatSqlParam(String sql, Object params) {
        if (params != null) {
            if (params instanceof Map paramMap) {
                Set<String> keys = paramMap.keySet();
                for (String key : keys) {
                    Object value = paramMap.get(key);
                    String valueStr = "null";
                    if (value instanceof Boolean booleanValue) {
                        valueStr = String.valueOf(booleanValue);
                    } else if (value instanceof LocalDate localDate) {
                        valueStr = "'" + localDate.format(DateUtil.DATE_FORMATTER) + "'";
                    } else if (value instanceof Date date) {
                        valueStr = "'" + DateUtil.formatDateTime(date) + "'";
                    } else if (value instanceof String str) {
                        valueStr = "'" + str.replace("'", "''") + "'";
                    } else if (value instanceof Number number) {
                        valueStr = number.toString();
                    } else {
                        if (value != null) {
                            log.error("param type {" + params.getClass().getSimpleName() + "} not support!");
                        }
                    }
                    sql = sql.replace("#" + key + "#", valueStr);
                }
            } else {
                throw new BaseException("param type {" + params.getClass().getSimpleName() + "} not support!").record();
            }
        }
        return sql;
    }

    protected List<Map<String, Object>> query(String sessionId, Statement statement, JdbcOption option, String querySql) {
        return query(sessionId, statement, option, querySql, null);
    }

    protected List<Map<String, Object>> query(String sessionId, Statement statement, JdbcOption option, String querySql, Map<String, Object> sqlParams) {
        if (StringUtil.isEmpty(querySql)) {
            throw new BaseException(JdbcError.CON_005.getMessage()).record();
        }
        //替换sql参数
        querySql = formatSqlParam(querySql, sqlParams);
        String sql = querySql.trim().replace("(", "").toUpperCase();
        if ((sql.startsWith("UPDATE") && !sql.contains("RETURNING"))
                || (sql.startsWith("DELETE") && !sql.contains("RETURNING"))) {
            throw new BaseException(JdbcError.CON_005.getMessage()).record();
        }
        this.printSql(sessionId, option, querySql);
        List<Map<String, Object>> result;
        ResultSet resultSet = null;
        long start = System.currentTimeMillis();
        try {
            resultSet = statement.executeQuery(querySql);
            if (option.getTest()) {
                long end = System.currentTimeMillis();
                log.info("QUERY[ 1 ]: " + (end - start));
            }
            long start2 = System.currentTimeMillis();
            result = resultSetToMap(option, resultSet);
            if (option.getTest()) {
                long end2 = System.currentTimeMillis();
                log.info("QUERY[ 2 ]: " + (end2 - start2));
            }
            if (option.getLog()) {
                log.info("SESSION: {} -------> 查询结果行数: {} 行结果 \n", sessionId, result.size());
            }
            return result;
        } catch (SQLException e) {
            printError(e, sessionId, querySql);
            throw new BaseException(e);
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > 1000) {
                log.warn("警告: {} SQL执行时间: {} | {} ", sessionId, time, querySql);
            } else if (option.getLog()) {
                log.info("{} SQL执行时间: {} ", sessionId, time);
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.error(log, e, "关闭resultSet失败: ");
                }
            }
        }
    }


    protected List<Map<String, Object>> insert(String sessionId, Statement statement, String sql) {
        return insert(sessionId, statement, sql, null);
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, String sql, Map<String, Object> sqlParams) {
        if (StringUtil.isEmpty(sql)) {
            throw new BaseException(JdbcError.CON_005.getMessage()).record();
        }
        sql = this.formatSqlParam(sql, sqlParams);
        this.printSql(sessionId, option, sql);
        List<Map<String, Object>> result;
        ResultSet resultSet;
        long start = System.currentTimeMillis();
        try {
            if (!sql.trim().toUpperCase().contains(" RETURNING ")) {
                if (sql.contains(";")) {
                    sql = sql.replace(";", " RETURNING * ;");
                } else {
                    sql = sql + " RETURNING * ;";
                }
            }
            resultSet = statement.executeQuery(sql);
            result = resultSetToMap(option, resultSet);
            if (option.getLog()) {
                LOGGER.info(log, "SESSION: %s -------> 插入结果行数: %s 行结果 %n", sessionId, result.size());
            }
            return result;
        } catch (SQLException e) {
            printError(e, sessionId, sql);
            throw new BaseException(e);
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > 1000) {
                log.warn("警告: {} SQL执行时间: {} | {} ", sessionId, time, sql);
            } else if (option.getLog()) {
                log.info("{} SQL执行时间: {} ", sessionId, time);
            }
        }
    }

    protected void execute(String sessionId, Statement statement, JdbcOption option, String sql) {
        execute(sessionId, statement, option, sql, null);
    }

    protected void execute(String sessionId, Statement statement, JdbcOption option, String sql, Map<String, Object> sqlParams) {
        if (StringUtil.isEmpty(sql)) {
            throw new BaseException(JdbcError.CON_005.getMessage()).record();
        }
        sql = this.formatSqlParam(sql, sqlParams);
        this.printSql(sessionId, option, sql);
        long start = System.currentTimeMillis();
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            printError(e, sessionId, sql);
            throw new BaseException(e);
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > 1000) {
                log.warn("警告: {} SQL执行时间: {} | {} ", sessionId, time, sql);
            } else if (option.getLog()) {
                log.info("{} SQL执行时间: {} ", sessionId, time);
            }
        }
    }

    private void printSql(String sessionId, JdbcOption option, String sql) {
        if (option.getLog()) {
            LOGGER.info(log, "SESSION: %s: %s%n", sessionId, sql);
        }
    }

    private void printError(SQLException exception, String sessionId, String sql) {
        LOGGER.error(log, exception, "SESSION: %s: %s%n", sessionId, sql);
    }


    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, String tableName, Map<String, Object> resource) {
        return insert(sessionId, statement, option, tableName, resource, new InsertOptions());
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, String tableName, Map<String, Object> resource, InsertOptions insertOptions) {
        String insertSql = sqlBuilder.insert(tableName, resource, insertOptions.getConflictColumns());
        if (insertSql == null) {
            return new ArrayList<>();
        }
        return insertExecute(sessionId, statement, insertSql);
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, String tableName, List<Map<String, Object>> resources) {
        return insert(sessionId, statement, option, tableName, resources, new InsertOptions());
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        String insertSql = sqlBuilder.insert(tableName, resources, insertOptions.getConflictColumns());
        if (insertSql == null) {
            return new ArrayList<>();
        }
        return insertExecute(sessionId, statement, insertSql);
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, String tableName, Set<String> columns, List<Map<String, Object>> resources) {
        return insert(sessionId, statement, option, tableName, columns, resources, new InsertOptions());
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        String insertSql = sqlBuilder.insert(tableName, columns, resources, insertOptions.getConflictColumns());
        if (insertSql == null) {
            return new ArrayList<>();
        }
        return insertExecute(sessionId, statement, insertSql);
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, Object resource) {
        return insert(sessionId, statement, option, resource, new InsertOptions());
    }

    protected List<Map<String, Object>> insert(String sessionId, Statement statement, JdbcOption option, Object resource, InsertOptions insertOptions) {
        String insertSql = sqlBuilder.insert(resource, insertOptions.getConflictColumns());
        if (insertSql == null) {
            return new ArrayList<>();
        }
        if (insertOptions.getAutoCreateTableIfNotExist()) {
            String createTableSql = sqlBuilder.createTable(resource);
            if (StringUtil.isEmpty(createTableSql)) {
                throw new BaseException("Failed to create table");
            } else if (!createdTableSet.contains(createTableSql)) {
                execute(sessionId, statement, option, createTableSql);
            }
            List<Map<String, Object>> data = insertExecute(sessionId, statement, insertSql);
            createdTableSet.add(createTableSql);
            return data;
        }
        return insertExecute(sessionId, statement, insertSql);
    }

    private List<Map<String, Object>> insertExecute(String sessionId, Statement statement, String insertSql) {
        if (insertSql.contains("RETURNING *")) {
            return query(sessionId, statement, option, insertSql);
        }
        execute(sessionId, statement, option, insertSql);
        return new ArrayList<>();
    }

    protected void createTable(String sessionId, Statement statement, JdbcOption option, Object resource) {
        String createTableSql = sqlBuilder.createTable(resource);
        execute(sessionId, statement, option, createTableSql);
    }

    protected int update(String sessionId, Statement statement, JdbcOption option, List<UpdateCondition> updateConditions) {
        String updateSql = sqlBuilder.update(updateConditions);
        if (updateSql == null) {
            return 0;
        }
        return update(sessionId, statement, option, updateSql);
    }

    protected <T> int update(String sessionId, Statement statement, JdbcOption option, UpdateCondition<T> updateCondition) {
        String updateSql = sqlBuilder.update(updateCondition);
        if (updateSql == null) {
            return 0;
        }
        return update(sessionId, statement, option, updateSql);
    }


    protected <T> List<T> toBeanList(List<Map<String, Object>> data, Class<T> clazz) {
        try {
            // ② 直接调用（不需要 setAccessible，因为它已经是 public）
            return new ArrayList<>(data.stream().map(map -> {
                return toBean(map, clazz);
            }).toList());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("toBeanList 构造对象异常$2");
            throw new BaseException(e);
        }
    }

    private static Map<String, Constructor> ctorMap = new ConcurrentHashMap<>();
    private static Map<String, Method> methodMap = new ConcurrentHashMap<>();

    protected <T> T toBean(Map<String, Object> map, Class<T> clazz) {
        long l = System.currentTimeMillis();
        try {
            String name = clazz.getName();
            Constructor<T> ctor = ctorMap.computeIfAbsent(name, v -> {
                try {
                    return clazz.getConstructor();
                } catch (NoSuchMethodException e) {
                    log.error("toBean 构造对象异常$1:ctor");
                    throw new BaseException(e);
                }
            });
            Method method = methodMap.computeIfAbsent(name, v -> {
                try {
                    Method initMethod = clazz.getDeclaredMethod("initFromMap", Map.class);
                    initMethod.setAccessible(true);
                    return initMethod;
                } catch (NoSuchMethodException e) {
                    log.error("toBean 构造对象异常$1:method");
                    throw new BaseException(e);
                }
            });
            T t = ctor.newInstance();
            method.invoke(t, map);
            return t;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("toBean 构造对象异常$1");
            throw new BaseException(e);
        } finally {
            long l2 = System.currentTimeMillis();
            log.info("构建对象耗时: {}", (l2 - l));
        }
    }
}
