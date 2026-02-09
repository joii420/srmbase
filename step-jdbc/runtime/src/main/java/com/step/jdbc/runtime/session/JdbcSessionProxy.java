package com.step.jdbc.runtime.session;


import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.session.options.InsertOptions;
import com.step.jdbc.runtime.session.support.JdbcSqlBuilder;
import com.step.jdbc.runtime.session.support.UpdateCondition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JdbcSessionProxy extends BaseJdbcSession implements JdbcSessionInterface {

    private final String sessionId;
    private final Statement statement;
    private final JdbcOption option;

    JdbcSessionProxy(String sessionId, Statement statement, JdbcOption option, JdbcSqlBuilder sqlBuilder) {
        super(sqlBuilder, option);
        this.sessionId = sessionId;
        this.statement = statement;
        this.option = option;
    }

    @Override
    public void execute(String executeSql, Map<String, Object> sqlParams) {
        execute(this.sessionId, this.statement, option, executeSql, sqlParams);
    }

    @Override
    public List<Map<String, Object>> insertSql(String insertSql, Map<String, Object> sqlParams) {
        return insert(this.sessionId, this.statement, insertSql, sqlParams);
    }

    @Override
    public int update(String updateSql, Map<String, Object> sqlParams) {
        return update(this.sessionId, this.statement, option, updateSql, sqlParams);
    }

    @Override
    public <T> T queryOne(String querySql, Map<String, Object> sqlParams, Class<T> clazz) {
        if (clazz == null) {
            throw new BaseException("class is null").record();
        }
        if (!BaseJdbcEntity.class.isAssignableFrom(clazz)) {
            throw new BaseException("class{" + clazz.getSimpleName() + "} not extend BaseEntity").record();
        }
        List<Map<String, Object>> data = query(this.sessionId, this.statement, option, querySql, sqlParams);
        if (data == null || data.isEmpty()) {
            return null;
        }
        if (data.size() > 1) {
            throw new BaseException(this.sessionId + ": 记录不唯一!").record();
        }
        Map<String, Object> row = data.get(0);
        try {
            // ② 直接调用（不需要 setAccessible，因为它已经是 public）
            return toBean(row, clazz);
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    @Override
    public List<Map<String, Object>> query(String querySql, Map<String, Object> sqlParams) {
        return query(this.sessionId, this.statement, option, querySql, sqlParams);
    }

    @Override
    public <T> List<T> query(String querySql, Map<String, Object> sqlParams, Class<T> clazz) {
        List<Map<String, Object>> data = query(this.sessionId, this.statement, option, querySql, sqlParams);
        return toBeanList(data, clazz);
    }

    @Override
    public int queryCount(String updateSql, Map<String, Object> sqlParams) {
        return queryCount(this.sessionId, this.statement, option, updateSql, sqlParams);
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Map<String, Object> resource) {
        return insert(this.sessionId, statement, option, tableName, resource);
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Map<String, Object> resource, InsertOptions insertOptions) {
        return insert(this.sessionId, statement, option, tableName, resource, insertOptions);
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources) {
        return insert(this.sessionId, statement, option, tableName, resources);
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        return insert(this.sessionId, statement, option, tableName, resources, insertOptions);
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources) {
        return insert(this.sessionId, statement, option, tableName, columns, resources);
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        return insert(this.sessionId, statement, option, tableName, columns, resources, insertOptions);
    }

    @Override
    public List<Map<String, Object>> insert(Object resource) {
        if (resource instanceof String insertSql) {
            return this.insertSql(insertSql);
        }
        return insert(this.sessionId, statement, option, resource);
    }

    @Override
    public List<Map<String, Object>> insert(Object resource, InsertOptions insertOptions) {
        return insert(this.sessionId, statement, option, resource, insertOptions);
    }

    @Override
    public int update(List<UpdateCondition> updateConditions) {
        return update(this.sessionId, statement, option, updateConditions);
    }

    @Override
    public <T> int update(UpdateCondition<T> updateCondition) {
        return update(this.sessionId, statement, option, updateCondition);
    }

    @Override
    public void createTable(Object resource) {
        createTable(this.sessionId, statement, option, resource);
    }


}