package com.step.jdbc.runtime.session.support;

import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2024/4/23  19:58
 */
public class PostgresSqlBuilder extends BaseSqlBuilder implements JdbcSqlBuilder {
    PostgresSqlBuilder() {
    }

    private final String createTableSql = """
            CREATE TABLE IF NOT EXISTS #scheme#"#tableName#" (
              #columns#
              #primaryKey#
            );
            """;
    private final String primaryKeySql = """
            ,
            CONSTRAINT "#tableName#_pkey" PRIMARY KEY ("#primaryKey#")
            """;

    private final String createIndexSql = """
            CREATE #unique# INDEX #concurrently#  IF NOT EXISTS "#tableName#_#indexName#" ON #scheme#"#tableName#" USING #type# (
              "#columns#"
            );
            """;

    @Override
    public String createTable(Object obj) {
        Object resource;
        if (obj instanceof List<?> list) {
            resource = list.get(0);
        } else {
            resource = obj;
        }
        String sql = createTableSql;
        String indexSql = createIndexSql;
        Set<String> primaryKeySet = new HashSet<>(0);
        JdbcField jdbcClass = getJdbcClass(resource);
        String scheme = jdbcClass.getScheme();
        if (StringUtil.isNotEmpty(scheme)) {
            sql = sql.replace("#scheme#", "\"" + scheme + "\".");
            indexSql = indexSql.replace("#scheme#", "\"" + scheme + "\".");
        } else {
            sql = sql.replace("#scheme#", "");
            indexSql = indexSql.replace("#scheme#", "");
        }
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
        indexSql = indexSql.replace("#tableName#", tableName);
        Map<String, JdbcFieldProperty> fields = jdbcClass.getFields();
        String totalColumns = fields.values().stream().sorted(Comparator.comparing(JdbcFieldProperty::getSort)).map(property -> {
            String column = property.getColumn();
            if (property.isPrimaryKey()) {
                primaryKeySet.add(column);
                if (property.getPrimaryKeyType() == PrimaryKeyType.AUTO_INCREMENT) {
                    return " \"" + column + "\" bigserial";
                }
            }
            String fieldType = getFieldType(property);
            return " \"" + column + "\" " + fieldType;
        }).collect(Collectors.joining(",\n"));
        sql = sql.replace("#columns#", totalColumns);
        if (primaryKeySet.size() == 0) {
            sql = sql.replace("#primaryKey#", "");
        } else {
            String keys = String.join("\",\"", primaryKeySet);
            String keyReplace = primaryKeySql.replace("#primaryKey#", keys).replace("#tableName#", tableName);
            sql = sql.replace("#primaryKey#", keyReplace);
        }
        Map<String, JdbcIndexProperty> indexes = jdbcClass.getIndexes();
        if (CollectionUtil.isNotEmpty(indexes)) {
            for (JdbcIndexProperty index : indexes.values()) {
                String columns = String.join("\",\"", index.getColumns());
                IndexType type = index.getType();
                Boolean concurrently = index.getConcurrently();
                Boolean unique = index.getUnique();
                String name = index.getName();

                indexSql = indexSql.replace("#unique#", unique ? "UNIQUE" : "")
                        .replace("#concurrently#", concurrently ? "CONCURRENTLY" : "")
                        .replace("#indexName#", name)
                        .replace("#type#", type.getValue())
                        .replace("#columns#", columns);
                sql = sql.concat("\n").concat(indexSql);
            }
        }
        return sql;
    }

    private String getFieldType(JdbcFieldProperty property) {
        JdbcFieldType type = property.getType();
        String length = property.getLength();
        switch (type) {
            case STRING -> {
                if (Objects.equals(length, "-1")) {
                    return "text";
                }
                return "varchar(" + length + ")";
            }
            case BOOL -> {
                return "bool";
            }
            case DATE, LOCAL_DATE -> {
                return "date";
            }
            case INTEGER -> {
                return "int4";
            }
            case LONG -> {
                return "int8";
            }
            case DOUBLE, NUMBER -> {
                return "numeric(" + length + ")";
            }
            case LOCAL_DATE_TIME, TIMESTAMP -> {
                return "timestamp(6)";
            }
            default -> {
                return "varchar(50)";
            }
        }
    }
}
