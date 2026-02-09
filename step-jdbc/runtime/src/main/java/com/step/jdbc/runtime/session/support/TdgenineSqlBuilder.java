package com.step.jdbc.runtime.session.support;


import com.step.api.runtime.exception.base.BaseException;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2024/4/23  19:58
 */
public class TdgenineSqlBuilder extends BaseSqlBuilder implements JdbcSqlBuilder {
    TdgenineSqlBuilder() {
    }
    private final static String INSERT_TEMPLATE = "INSERT INTO #tableName# (\"#columns#\") VALUES (#values#);";

    private final String createTableSql = """
            CREATE TABLE IF NOT EXISTS #tableName# (
              #columns#
            );
            """;

    @Override
    public String insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, Set<String> conflictColumns) {
        if (StringUtil.isEmpty(tableName)) {
            throw new BaseException("TableName can not be null").record();
        }
        if (CollectionUtil.isEmpty(resources)) {
            return null;
        }
        final Set<String> finalColumns = new TreeSet<>(columns);
        List<String> valuesList = new ArrayList<>();
        resources.forEach(resource -> {
            List<String> valueList = new ArrayList<>();
            finalColumns.forEach(column -> {
                Object value = resource.get(column);
                valueList.add(formatValue(value));
            });
            String values = String.join(",", valueList);
            valuesList.add(values);
        });
        String column = String.join("\", \"", finalColumns);
        String values = String.join("), (", valuesList);
        return INSERT_TEMPLATE.replace("#tableName#", tableName).replace("#columns#", column).replace("#values#", values);
    }

    @Override
    public String insert(List<?> resources, Set<String> conflictColumns) {
        if (CollectionUtil.isEmpty(resources)) {
            return null;
        }
        Object obj = resources.get(0);
        JdbcField jdbcField = getJdbcClass(obj);
        String tableName = jdbcField.getTableName();
        Map<String, JdbcFieldProperty> columnMap = jdbcField.getFields();
        Set<String> columns = columnMap.keySet().stream().filter(c -> !columnMap.get(c).getInsertIgnore()).collect(Collectors.toSet());
        List<String> valuesList = new ArrayList<>();
        resources.forEach(resource -> {
            List<String> valueList = getInsertValues(resource, columnMap, columns);
            String values = String.join(",", valueList);
            valuesList.add(values);
        });
        String column = String.join("\", \"", columns);
        String values = String.join("), (", valuesList);
        return INSERT_TEMPLATE.replace("#tableName#", tableName).replace("#columns#", column).replace("#values#", values);
    }

    @Override
    public String createTable(Object obj) {
        Object resource;
        if (obj instanceof List<?> list) {
            resource = list.get(0);
        } else {
            resource = obj;
        }
        String sql = createTableSql;
        Set<String> primaryKeySet = new HashSet<>(0);
        JdbcField jdbcClass = getJdbcClass(resource);
        String tableName;
        //动态表名称
        if (jdbcClass.getDynamicsTableName() && jdbcClass.getDynamicsTableNameMethod() != null) {
            try {
                tableName = (String) jdbcClass.getDynamicsTableNameMethod().invoke(resource);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            tableName = jdbcClass.getTableName();
        }
        sql = sql.replace("#tableName#", tableName);
        Map<String, JdbcFieldProperty> fields = jdbcClass.getFields();
        String totalColumns = fields.values().stream().sorted(Comparator.comparing(JdbcFieldProperty::getSort)).map(property -> {
            String column = property.getColumn();
            if (property.isPrimaryKey()) {
                primaryKeySet.add(column);
            }
            String fieldType = getFieldType(property);
            return " " + column + " " + fieldType;
        }).collect(Collectors.joining(",\n"));
        sql = sql.replace("#columns#", totalColumns);
        return sql;
    }

    private String getFieldType(JdbcFieldProperty property) {
        JdbcFieldType type = property.getType();
        String length = property.getLength();
        switch (type) {
            case STRING -> {
                return "VARCHAR(" + length + ")";
            }
            case DATE, LOCAL_DATE -> {
                return "DATE";
            }
            case LONG -> {
                return "LONG";
            }
            case INTEGER -> {
                return "INT";
            }
            case DOUBLE,NUMBER -> {
                return "DOUBLE";
            }
            case LOCAL_DATE_TIME, TIMESTAMP -> {
                return "TIMESTAMP";
            }
            default -> {
                return "VARCHAR(50)";
            }
        }
    }
}
