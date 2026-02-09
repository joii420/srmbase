package com.step.jdbc.runtime.flink.dictionary.utils;

import com.step.jdbc.runtime.flink.dictionary.database.tdengine.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class TDengineUtils {

//    private  final String URL = "jdbc:TAOS-RS://47.98.171.39:6041/testDb?user=root&password=taosdata";

    private Connection connection;

    {
        try {
            Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
//            connection = DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TDengineUtils(TDengineDatabase tDengineDatasource) {
        String jdbcUrl = String.format("jdbc:TAOS-RS://%s:%d/%s?user=%s&password=%s", tDengineDatasource.getHost(), tDengineDatasource.getPort(), tDengineDatasource.getDatabase(), tDengineDatasource.getUsername(), tDengineDatasource.getPassword());
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            if (e.getMessage().contains("Database not exist")) {
                //创建数据库
                try {
                    Connection conn = DriverManager.getConnection(String.format("jdbc:TAOS-RS://%s:%s/",tDengineDatasource.getHost(), tDengineDatasource.getPort()), tDengineDatasource.getUsername(), tDengineDatasource.getPassword());
                    Statement stmt = conn.createStatement();
                    String sql = "CREATE DATABASE " + tDengineDatasource.getDatabase();
                    stmt.executeUpdate(sql);
                    System.out.println("Database created successfully.");
                    stmt.close();
                    connection = DriverManager.getConnection(jdbcUrl);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public Field[] getAllFields(Class<?> entityClass) {
        List<Field> fields = new ArrayList<>();
        // 获取父类的字段
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null) {
            Field[] superFields = getAllFields(superClass);
            fields.addAll(Arrays.asList(superFields));
        }
        // 获取当前类声明的字段
        Field[] declaredFields = entityClass.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));


        return fields.toArray(new Field[0]);
    }

    public void createTable(String tableName, String pkField, List<ColumnInfo> columns) {
        StringBuilder createSqlBuilder = new StringBuilder("ts TIMESTAMP ,");
        columns.stream().sorted(Comparator.comparingInt(c -> Integer.parseInt(c.getField()))).forEach(c -> {
            String fieldName = c.getColumn();
            DataType type = c.getType();
            createSqlBuilder.append(fieldName).append(" ").append(type.getSqlType()).append(", ");
        });
        String create = createSqlBuilder.toString();
        create = create.substring(0, create.length() - 2);
        try (Statement stmt = connection.createStatement()) {

            String sql = "CREATE TABLE " + tableName + " (" + create + ");"
                    + "  ALTER TABLE " + tableName + " SET interval (1m);"
                    + " CREATE INDEX index_name ON " + tableName + " ('" + pkField + "'）";
            stmt.executeUpdate(sql);
            System.out.println("Table created: " + tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(Connection conn, Class<?> entityClass, String tableName) {
        StringBuilder schemaBuilder = new StringBuilder();
        Field[] fields = getAllFields(entityClass);
        for (Field field : fields) {
            String fieldName = field.getName();
//            String fieldType = field.getType().getSimpleName();
            Class<?> fieldType = field.getType();
            schemaBuilder.append(fieldName).append(" ").append(getSQLType(fieldType)).append(", ");
        }
        String schema = schemaBuilder.toString();
        schema = schema.substring(0, schema.length() - 2); // Remove the trailing comma
        try (Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE " + tableName + " (" + schema + ");";
            stmt.executeUpdate(sql);
            System.out.println("Table created: " + tableName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Class<?> entityClass, String tableName) {
//        String tableName = entityClass.getSimpleName();
        createTable(getConnection(), entityClass, tableName);
    }

    public List<Object> query(Class<? extends BaseMachine> entityClass, String tableName, List<Condition> condition) {
        List<Object> resultList = new ArrayList<>();


        try (Connection conn = getConnection();

             Statement stmt = conn.createStatement()) {
//            String tableName = entityClass.getField("tableName").getName();
            StringBuilder conditionStr = new StringBuilder();
            conditionStr.append("1=1");
            for (int i = 0; i < condition.size(); i++) {
                conditionStr.append(" AND ");
                conditionStr.append(buildConditionString(condition.get(i)));
            }

            String sql = "SELECT * FROM " + tableName + " WHERE " + conditionStr;
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                Object entity = entityClass.getDeclaredConstructor().newInstance();
                Field[] fields = getAllFields(entityClass);
                for (Field field : fields) {
                    String fieldName = field.getName().toLowerCase();
                    Object value = resultSet.getObject(fieldName);
                    field.setAccessible(true);
                    field.set(entity, value);
                }
                resultList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public <T> List<T> query(Class<T> entityClass, String[] resfieldsName, String sql) {

        List<T> resultList = new ArrayList<>();
        try (Connection conn = getConnection();

             Statement stmt = conn.createStatement()) {
//            String tableName = entityClass.getField("tableName").getName();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                T entity = entityClass.getDeclaredConstructor().newInstance();
//                Field[] fields = getAllFields(entityClass);
//                for (Field field : fields) {
//                    String fieldName = field.getName().toLowerCase();
//                    Object value = resultSet.getObject(fieldName);
//                    field.setAccessible(true);
//                    field.set(entity, value);
//                }

                for (int i = 0; i < resfieldsName.length; i++) {
                    Field field = entity.getClass().getField(resfieldsName[i].toLowerCase());
                    if (field == null) {
                        continue;
                    }
                    Object value = resultSet.getObject(field.getName());
                    field.setAccessible(true);
                    field.set(entity, value);
                }
                resultList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<Map<String, Object>> query(String sql) {
        List<Map<String, Object>> result = new ArrayList<>();
        Connection conn = getConnection();

        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //遍历加入
            while (resultSet.next()) {
                Map<String, Object> maps = new TreeMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    maps.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                }
                result.add(maps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> query(String[] resfieldsName, String sql) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                Map<String, Object> entityMap = new HashMap<>();
                for (int i = 0; i < resfieldsName.length; i++) {
                    Object value = resultSet.getObject(resfieldsName[i]);
                    entityMap.put(resfieldsName[i], value);
                }
                resultList.add(entityMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private String buildConditionString(Condition condition) {
        String fieldName = condition.getFieldName();
        String operator = condition.getOperator();
        Object value = condition.getValue();

        StringBuilder conditionStr = new StringBuilder();

        if (operator.equals(">")) {
            conditionStr.append(fieldName).append(" > ").append(value);
        } else if (operator.equals("=")) {
            conditionStr.append(fieldName).append(" = ").append(value);
        } else if (operator.equals("<")) {
            conditionStr.append(fieldName).append(" < ").append(value);
        } else if (operator.equals("between")) {
            if (value instanceof List) {
                List<?> rangeValues = (List<?>) value;
                if (rangeValues.size() == 2) {
                    conditionStr.append(fieldName).append(" BETWEEN ")
                            .append(rangeValues.get(0)).append(" AND ").append(rangeValues.get(1));
                } else {
                    // 处理区间条件值不完整的情况
                }
            } else {
                // 处理区间条件值类型不正确的情况
            }
        } else {
            // 处理其他操作符
        }

        return conditionStr.toString();
    }

    public void insert(Connection conn, Object entity, String tableName) {
        Class<?> entityClass = entity.getClass();
//        String tableName = entityClass.getSimpleName();
        StringBuilder valuesBuilder = new StringBuilder();

        Field[] fields = getAllFields(entityClass);
        for (Field field : fields) {
            String fieldName = field.getName();
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(entity);
//                if (fieldName.equals("ts")) {
//                    value = field.get(entity);
//                } else {
//                    Random random = new Random();
//                    value = random.nextInt(2);
//                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
            valuesBuilder.append("'").append(value).append("',");
        }
        String values = valuesBuilder.toString();
        values = values.substring(0, values.length() - 1); // Remove the trailing comma

//        try (Connection conn = getConnection();
//             Statement stmt = conn.createStatement()) {

        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
        insert(conn, sql, tableName);
//            stmt.executeUpdate(sql);
//            System.out.println("Data inserted into " + tableName);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void insert(Object entity, String tableName) {
        insert(getConnection(), entity, tableName);
    }

    public void insert(Connection conn, String sql, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Data inserted into " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String initSql(String tableName, Map<String, Object> dataMap) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();

        // 将 "ts" 字段放在最前面
        if (dataMap.containsKey("ts")) {
            fieldNames.add("ts");
            fieldValues.add(dataMap.get("ts"));
            dataMap.remove("ts");
        }

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            fieldNames.add(entry.getKey());
            fieldValues.add(entry.getValue());
        }

        sqlBuilder.append(String.join(", ", fieldNames));
        sqlBuilder.append(") VALUES (");

        List<String> valuePlaceholders = new ArrayList<>();
        for (int i = 0; i < fieldValues.size(); i++) {
            valuePlaceholders.add("?");
        }

        sqlBuilder.append(String.join(", ", valuePlaceholders));
        sqlBuilder.append(")");

        String insertSql = sqlBuilder.toString();

        System.out.println(insertSql);
        return insertSql;
    }

    private Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
                String jdbcUrl = "jdbc:TAOS-RS://localhost:6041/testDb?user=root&password=taosdata";
                connection = DriverManager.getConnection(jdbcUrl);
            }

            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> resultSetToMap(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        //遍历加入
        while (resultSet.next()) {
            Map<String, Object> maps = new TreeMap<>();
            for (int i = 1; i <= columnCount; i++) {
//                if (ObjectUtils.isNotEmpty(resultSet.getObject(i))) {
                maps.put(metaData.getColumnLabel(i), resultSet.getObject(i));
//                }
            }
            result.add(maps);
        }
        return result;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(String sql)  {
        Connection conn = getConnection();
        try (Statement stat = conn.createStatement()) {
            stat.execute(sql);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private final Map<Class<?>, String> fieldTypeMap = new HashMap<>();

    {
        fieldTypeMap.put(int.class, "INT");
        fieldTypeMap.put(Integer.class, "INT");
        fieldTypeMap.put(long.class, "BIGINT");
        fieldTypeMap.put(Long.class, "BIGINT");
        fieldTypeMap.put(float.class, "FLOAT");
        fieldTypeMap.put(Float.class, "FLOAT");
        fieldTypeMap.put(double.class, "DOUBLE");
        fieldTypeMap.put(Double.class, "DOUBLE");
        fieldTypeMap.put(boolean.class, "BOOLEAN");
        fieldTypeMap.put(Boolean.class, "BOOLEAN");
        fieldTypeMap.put(String.class, "NCHAR(255)");
        fieldTypeMap.put(Timestamp.class, "TIMESTAMP");
    }

    public String getSQLType(Class<?> fieldType) {
        if (fieldTypeMap.containsKey(fieldType)) {
            return fieldTypeMap.get(fieldType);
        }
        return "NCHAR(50)"; // 默认为NVARCHAR类型
    }
}