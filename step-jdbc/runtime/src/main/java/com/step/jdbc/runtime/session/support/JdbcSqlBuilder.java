package com.step.jdbc.runtime.session.support;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.DBType;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author : Sun
 * @date : 2024/4/23  19:58
 */
public interface JdbcSqlBuilder {

    String createTable(Object resource);

    default String insert(String tableName, Map<String, Object> resource) {
        return insert(tableName, resource, null);
    }

    default String insert(String tableName, Map<String, Object> resource, Set<String> conflictColumns) {
        return insert(tableName, List.of(resource), conflictColumns);
    }

    default String insert(String tableName, List<Map<String, Object>> resources) {
        return insert(tableName, resources, null);
    }

    default String insert(String tableName, List<Map<String, Object>> resources, Set<String> conflictColumns) {
        if (StringUtil.isEmpty(tableName)) {
            throw new BaseException("TableName can not be null").record();
        }
        if (CollectionUtil.isEmpty(resources)) {
            return null;
        }
        Set<String> columns = new TreeSet<>();
        resources.forEach(m -> columns.addAll(m.keySet()));
        return insert(tableName, columns, resources, conflictColumns);
    }

    String insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, Set<String> conflictColumns);

    default String insert(Object resource) {
        return insert(resource, null);
    }

    default String insert(Object resource, Set<String> conflictColumns) {
        if (resource == null) {
            return null;
        }
        if (resource instanceof List<?> list) {
            return insert(list, conflictColumns);
        }
        return insert(List.of(resource), conflictColumns);
    }

    String insert(List<?> resources, Set<String> conflictColumns);

    String update(List<UpdateCondition> updateConditions);

    <T> String update(UpdateCondition<T> updateCondition);


    static JdbcSqlBuilder from(DBType dbType) {
        switch (dbType) {
            case POSTGRESQL -> {
                return new PostgresSqlBuilder();
            }
            case TDGENINE -> {
                return new TdgenineSqlBuilder();
            }
            case ORACLE -> {
                return new OracleSqlBuilder();
            }
            default -> {
                throw new BaseException("UnSupport database type " + dbType.name()).record();
            }
        }
    }
}
