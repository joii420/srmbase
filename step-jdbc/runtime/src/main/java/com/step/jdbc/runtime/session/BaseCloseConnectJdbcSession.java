package com.step.jdbc.runtime.session;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.session.options.InsertOptions;
import com.step.jdbc.runtime.session.support.JdbcSqlBuilder;
import com.step.jdbc.runtime.session.support.UpdateCondition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author : Sun
 * @date : 2024/4/24  12:51
 */
public abstract class BaseCloseConnectJdbcSession extends BaseTransactionJdbcSession {

    protected BaseCloseConnectJdbcSession(String sessionId, Connection connection, Statement statement, JdbcOption option, JdbcSqlBuilder sqlBuilder, String mountPool) {
        super(sessionId, connection, statement, option, sqlBuilder, mountPool);
    }


    public int updateUnClose(String updateSql) {
        return updateUnClose(updateSql, null);
    }

    public int updateUnClose(String updateSql, Map<String, Object> sqlParams) {
        int res = super.updateUnCommit(updateSql, sqlParams);
        commit();
        return res;
    }


    public <T> T queryOneUnClose(String querySql, Class<T> clazz) {
        return queryOneUnClose(querySql, Map.of(), clazz);
    }

    public <T> T queryOneUnClose(String querySql, Map<String, Object> sqlParams, Class<T> clazz) {
        if (clazz == null) {
            throw new BaseException("class is null").record();
        }
        if (!BaseJdbcEntity.class.isAssignableFrom(clazz)) {
            throw new BaseException("class{" + clazz.getSimpleName() + "} not extend BaseEntity").record();
        }
        List<Map<String, Object>> maps = queryUnClose(querySql, sqlParams);
        if (maps == null || maps.isEmpty()) {
            return null;
        }
        if (maps.size() > 1) {
            throw new BaseException(this.getSessionId() + ": 记录不唯一!").record();
        }
        Map<String, Object> data = maps.get(0);
        try {
            // ② 直接调用（不需要 setAccessible，因为它已经是 public）
            return toBean(data, clazz);
        } catch (Exception e) {
            log.error("toBean 构造对象异常$2");
            throw new BaseException(e);
        }
    }

    public <T> List<T> queryUnClose(String querySql, Class<T> clazz) {
        return queryUnClose(querySql, Map.of(), clazz);
    }

    public <T> List<T> queryUnClose(String querySql, Map<String, Object> sqlParams, Class<T> clazz) {
        if (clazz == null) {
            throw new BaseException("class is null").record();
        }
        if (!BaseJdbcEntity.class.isAssignableFrom(clazz)) {
            throw new BaseException("class{" + clazz.getSimpleName() + "} not extend BaseEntity").record();
        }
        List<Map<String, Object>> maps = queryUnClose(querySql, sqlParams);
        return toBeanList(maps, clazz);
    }

    public List<Map<String, Object>> queryUnClose(String querySql) {
        return queryUnClose(querySql, Map.of());
    }

    public List<Map<String, Object>> queryUnClose(String querySql, Map<String, Object> sqlParams) {
        List<Map<String, Object>> result = queryUnCommit(querySql, sqlParams);
        commit();
        return result;
    }


    public void executeBatchUnClose(Consumer<JdbcSessionProxy> handler) {
        executeBatchUnCommit(handler);
        commit();
    }

    public <T> T executeBatchUnClose(Function<JdbcSessionProxy, T> handler) {
        T result = executeBatchUnCommit(handler);
        commit();
        return result;
    }

    public void executeUnClose(String sql) {
        executeUnClose(sql, null);
    }

    public void executeUnClose(String sql, Map<String, Object> sqlParams) {
        executeUnCommit(sql, sqlParams);
        commit();
    }

    public List<Map<String, Object>> insertSqlUnClose(String insertSql) {
        return insertSqlUnClose(insertSql, null);
    }

    public List<Map<String, Object>> insertSqlUnClose(String insertSql, Map<String, Object> sqlParams) {
        List<Map<String, Object>> result = insertSqlUnCommit(insertSql, sqlParams);
        commit();
        return result;
    }

    public List<Map<String, Object>> insertUnClose(String tableName, Map<String, Object> resource) {
        List<Map<String, Object>> results = insertUnCommit(tableName, resource, new InsertOptions());
        commit();
        return results;
    }

    public List<Map<String, Object>> insertUnClose(String tableName, Map<String, Object> resource, InsertOptions
            insertOptions) {
        List<Map<String, Object>> results = insertUnCommit(tableName, resource, insertOptions);
        commit();
        return results;
    }

    public List<Map<String, Object>> insertUnClose(String tableName, List<Map<String, Object>> resources) {
        List<Map<String, Object>> results = insertUnCommit(tableName, resources, new InsertOptions());
        commit();
        return results;
    }

    public List<Map<String, Object>> insertUnClose(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        List<Map<String, Object>> results = insertUnCommit(tableName, resources, insertOptions);
        commit();
        return results;
    }


    public List<Map<String, Object>> insertUnClose(String tableName, Set<String> columns, List<Map<String, Object>> resources) {
        List<Map<String, Object>> results = insertUnCommit(tableName, columns, resources, new InsertOptions());
        commit();
        return results;
    }

    public List<Map<String, Object>> insertUnClose(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        List<Map<String, Object>> results = insertUnCommit(tableName, columns, resources, insertOptions);
        commit();
        return results;
    }

    public List<Map<String, Object>> insertUnClose(Object resource) {
        List<Map<String, Object>> results = insertUnCommit(resource, new InsertOptions());
        commit();
        return results;
    }

    public List<Map<String, Object>> insertUnClose(Object resource, InsertOptions insertOptions) {
        List<Map<String, Object>> results = insertUnCommit(resource, insertOptions);
        commit();
        return results;
    }

    public void createTableUnClose(Object resource) {
        createTableUnCommit(resource);
        commit();
    }

    public <T> int updateUnClose(UpdateCondition<T> resource) {
        int i = updateUnCommit(resource);
        commit();
        return i;
    }

    public int updateUnClose(List<UpdateCondition> resources) {
        int i = updateUnCommit(resources);
        commit();
        return i;
    }

    public int updateUnClose(UpdateCondition... resources) {
        int i = updateUnCommit(Arrays.stream(resources).toList());
        commit();
        return i;
    }

}
