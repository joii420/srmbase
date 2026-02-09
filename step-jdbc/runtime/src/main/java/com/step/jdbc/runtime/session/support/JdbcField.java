package com.step.jdbc.runtime.session.support;


import com.step.jdbc.runtime.session.anno.JdbcIndex;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2024/4/24  8:50
 */
public class JdbcField {
    JdbcField() {
    }

    private String scheme;
    private Boolean isDynamicsTableName;
    private Method dynamicsTableNameMethod;
    private String tableName;
    private Map<String, JdbcFieldProperty> fields;
    private Map<String, JdbcIndexProperty> indexes;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Boolean getDynamicsTableName() {
        return isDynamicsTableName;
    }

    public void setDynamicsTableName(Boolean dynamicsTableName) {
        isDynamicsTableName = dynamicsTableName;
    }

    public Method getDynamicsTableNameMethod() {
        return dynamicsTableNameMethod;
    }

    public void setDynamicsTableNameMethod(Method dynamicsTableNameMethod) {
        this.dynamicsTableNameMethod = dynamicsTableNameMethod;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, JdbcFieldProperty> getFields() {
        return fields;
    }

    public void setFields(Map<String, JdbcFieldProperty> fields) {
        this.fields = fields;
    }

    public Map<String, JdbcIndexProperty> getIndexes() {
        return indexes;
    }

    public void setIndexes(Map<String, JdbcIndexProperty> indexes) {
        this.indexes = indexes;
    }
}
