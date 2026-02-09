package com.step.jdbc.runtime.session;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.session.options.InsertOptions;
import com.step.jdbc.runtime.session.support.JdbcSqlBuilder;
import com.step.jdbc.runtime.session.support.UpdateCondition;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : Sun
 * @date : 2023/3/12  14:47
 */
public interface JdbcSessionInterface {

    Logger log = LoggerFactory.getLogger(JdbcSessionInterface.class);

    default void execute(String executeSql) {
        execute(executeSql, null);
    }

    void execute(String executeSql, Map<String, Object> sqlParams);

    default int update(String updateSql) {
        return update(updateSql, null);
    }

    int update(String updateSql, Map<String, Object> sqlParams);

    <T> T queryOne(String querySql, Map<String, Object> sqlParams, Class<T> clazz);

    default List<Map<String, Object>> query(String querySql) {
        return query(querySql, null);
    }

    List<Map<String, Object>> query(String querySql, Map<String, Object> sqlParams);

    <T> List<T> query(String querySql, Map<String, Object> sqlParams, Class<T> clazz);

    default int queryCount(String querySql) {
        return queryCount(querySql, null);
    }

    int queryCount(String querySql, Map<String, Object> sqlParams);

    default List<Map<String, Object>> insert(String tableName, Map<String, Object> resource) {
        return insert(tableName, resource, new InsertOptions());
    }

    List<Map<String, Object>> insert(String tableName, Map<String, Object> resource, InsertOptions insertOptions);

    default List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources) {
        return insert(tableName, resources, new InsertOptions());
    }

    List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions);

    default List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources) {
        return insert(tableName, columns, resources, new InsertOptions());
    }

    List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions);

    default List<Map<String, Object>> insert(Object resource) {
        if (resource instanceof String insertSql) {
            return insertSql(insertSql);
        }
        return insert(resource, new InsertOptions());
    }

    List<Map<String, Object>> insert(Object resource, InsertOptions insertOptions);

    default List<Map<String, Object>> insertSql(String insertSql) {
        return insertSql(insertSql, null);
    }

    List<Map<String, Object>> insertSql(String insertSql, Map<String, Object> sqlParams);

    int update(List<UpdateCondition> updateConditions);

    default int update(UpdateCondition... updateConditions) {
        return update(Arrays.stream(updateConditions).toList());
    }

    <T> int update(UpdateCondition<T> updateCondition);

    void createTable(Object resource);
}
