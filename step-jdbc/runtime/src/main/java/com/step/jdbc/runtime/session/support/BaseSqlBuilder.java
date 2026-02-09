package com.step.jdbc.runtime.session.support;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.session.anno.*;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.DateUtil;
import com.step.tool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2024/4/24  8:47
 */
public abstract class BaseSqlBuilder {
    public static final Logger log = LoggerFactory.getLogger(BaseSqlBuilder.class);

    private final static String INSERT_TEMPLATE = "INSERT INTO #tableName# (\"#columns#\") VALUES (#values#) RETURNING *;";
    private final static String INSERT_ON_CONFLICT_TEMPLATE = "INSERT INTO #tableName# (\"#columns#\") VALUES (#values#) ON CONFLICT (#conflictColumn#) DO NOTHING RETURNING *;";
    private final static String UPDATE_TEMPLATE = "UPDATE #tableName# SET #updates# WHERE #conditions#;";
    private static final Map<String, JdbcField> CLASS_RESOURCE = new ConcurrentHashMap<>();
    private static final String NULL = "NULL";


    public String createTable(Object resource) {
        return null;
    }

    protected JdbcField getJdbcClass(Object obj) {
        Class<?> clazz = obj.getClass();
        String fullName = clazz.getName();
        JdbcField jdbcField = CLASS_RESOURCE.get(fullName);
        if (jdbcField == null) {
            //构建jdbc 表属性
            jdbcField = new JdbcField();
            JdbcScheme scheme = clazz.getDeclaredAnnotation(JdbcScheme.class);
            if (scheme != null) {
                jdbcField.setScheme(scheme.value());
            }
            JdbcDynamicsTable dynamicsTable = clazz.getDeclaredAnnotation(JdbcDynamicsTable.class);
            if (dynamicsTable == null || StringUtil.isEmpty(dynamicsTable.method())) {
                jdbcField.setDynamicsTableName(false);
                JdbcTable jdbcTable = clazz.getDeclaredAnnotation(JdbcTable.class);
                if (jdbcTable == null || StringUtil.isEmpty(jdbcTable.value())) {
                    String tableName = convertStrToSnakeCase(clazz.getSimpleName());
                    jdbcField.setTableName(tableName);
                } else {
                    jdbcField.setTableName(jdbcTable.value());
                }
            } else {
                try {
                    String value = dynamicsTable.method();
                    Method declaredMethod = clazz.getDeclaredMethod(value);
                    String tableName = (String) declaredMethod.invoke(obj);
                    jdbcField.setDynamicsTableName(true);
                    jdbcField.setDynamicsTableNameMethod(declaredMethod);
                    jdbcField.setTableName(tableName);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            //包含所有超类的属性
            List<Field> fields = getAllFields(clazz);
//            Field[] fields = clazz.getDeclaredFields();
            //构建列属性
            Map<String, JdbcFieldProperty> properties = new LinkedHashMap<>();
            Map<String, JdbcIndexProperty> indexes = new LinkedHashMap<>();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);
                JdbcIgnore jdbcIgnore = field.getDeclaredAnnotation(JdbcIgnore.class);
                if (jdbcIgnore != null) {
                    continue;
                }
                JdbcFieldProperty property = new JdbcFieldProperty();
                //定义排序
                property.setSort(i + 1);
                JdbcInsertIgnore insertIgnore = field.getDeclaredAnnotation(JdbcInsertIgnore.class);
                property.setInsertIgnore(insertIgnore != null);
                String name = field.getName();
                property.setField(name);
                //定义主键
                JdbcPrimaryKey primaryKey = field.getDeclaredAnnotation(JdbcPrimaryKey.class);
                property.setPrimaryKey(primaryKey != null);
                if (primaryKey != null) {
                    property.setPrimaryKeyType(primaryKey.value());
                    if (primaryKey.value() == PrimaryKeyType.AUTO_INCREMENT) {
                        property.setInsertIgnore(true);
                    }
                }
                //定义栏位名称
                JdbcColumn jdbcColumn = field.getDeclaredAnnotation(JdbcColumn.class);
                String column = jdbcColumn == null ? convertStrToSnakeCase(name) : jdbcColumn.value();
                property.setColumn(column);
                Class<?> typeClazz = field.getType();
                String typeName = typeClazz.getSimpleName();
                //定义默认值
                JdbcDefaultValue jdbcDefaultValue = field.getDeclaredAnnotation(JdbcDefaultValue.class);
                if (jdbcDefaultValue != null) {
                    property.setDefaultValue(jdbcDefaultValue.value());
                }
                //定义长度
                JdbcFieldLength fieldLength = field.getDeclaredAnnotation(JdbcFieldLength.class);
                if (fieldLength != null && StringUtil.isNotEmpty(fieldLength.value())) {
                    property.setLength(fieldLength.value());
                }
                //收集索引
                JdbcIndex jdbcIndex = field.getDeclaredAnnotation(JdbcIndex.class);
                if (jdbcIndex != null) {
                    String indexName = jdbcIndex.name();
                    if (StringUtil.isEmpty(indexName)) {
                        indexName = "index_def";
                    }
                    JdbcIndexProperty jdbcIndexProperty = indexes.get(indexName);
                    if (jdbcIndexProperty == null) {
                        jdbcIndexProperty = new JdbcIndexProperty();
                        jdbcIndexProperty.setType(jdbcIndex.type());
                        jdbcIndexProperty.setName(indexName);
                        jdbcIndexProperty.setUnique(jdbcIndex.unique());
                        jdbcIndexProperty.setConcurrently(jdbcIndex.concurrently());
                        String indexColumn = property.getColumn();
                        List<String> indexColumns = new ArrayList<>();
                        indexColumns.add(indexColumn);
                        jdbcIndexProperty.setColumns(indexColumns);
                        indexes.put(indexName, jdbcIndexProperty);
                    } else {
                        //增加属性
                        jdbcIndexProperty.getColumns().add(property.getColumn());
                    }
                }
                if (typeClazz == String.class || typeClazz.isEnum()) {
                    property.setType(JdbcFieldType.STRING);
                    property.setLength(Optional.ofNullable(property.getLength()).orElse("50"));
                } else if (typeClazz == Date.class || typeClazz == LocalDate.class || typeClazz == LocalDateTime.class || typeClazz == Timestamp.class) {
                    if (property.getDefaultValue() != null) {
                        System.out.println("警告: Field type of Date can not has default value!! !");
                        property.setDefaultValue(null);
                    }
                    if (typeClazz == Date.class) {
                        property.setType(JdbcFieldType.DATE);
                    } else if (typeClazz == LocalDate.class) {
                        property.setType(JdbcFieldType.LOCAL_DATE);
                    } else if (typeClazz == LocalDateTime.class) {
                        property.setType(JdbcFieldType.LOCAL_DATE_TIME);
                    } else if (typeClazz == Timestamp.class) {
                        property.setType(JdbcFieldType.TIMESTAMP);
                    }
                    JdbcDateFormat dateFormatter = field.getDeclaredAnnotation(JdbcDateFormat.class);
                    if (dateFormatter != null) {
                        property.setDateFormat(dateFormatter.format());
                    } else {
                        property.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                } else if (typeClazz == Boolean.class) {
                    property.setType(JdbcFieldType.BOOL);
                    property.setLength("");
                } else if (typeClazz == Integer.class) {
                    property.setType(JdbcFieldType.INTEGER);
                    property.setLength(Optional.ofNullable(property.getLength()).orElse("10,0"));
                } else if (typeClazz == Long.class) {
                    property.setType(JdbcFieldType.LONG);
                    property.setLength(Optional.ofNullable(property.getLength()).orElse("20,0"));
                } else if (typeClazz == Float.class) {
                    property.setType(JdbcFieldType.DOUBLE);
                    property.setLength(Optional.ofNullable(property.getLength()).orElse("10,4"));
                } else if (typeClazz == Double.class) {
                    property.setType(JdbcFieldType.DOUBLE);
                    property.setLength(Optional.ofNullable(property.getLength()).orElse("20,6"));
                } else if (typeClazz == BigDecimal.class) {
                    property.setType(JdbcFieldType.NUMBER);
                    property.setLength(Optional.ofNullable(property.getLength()).orElse("20,6"));
                } else {
                    System.out.println("Class " + fullName + " Ignore Type " + typeName + " | Field " + name);
                    continue;
                }


                properties.put(column, property);
            }
            jdbcField.setFields(properties);
            jdbcField.setIndexes(indexes);
            CLASS_RESOURCE.put(fullName, jdbcField);
        } else {
            if (jdbcField.getDynamicsTableName() && jdbcField.getDynamicsTableNameMethod() != null) {
                try {
                    String tableName = (String) jdbcField.getDynamicsTableNameMethod().invoke(obj);
                    jdbcField.setTableName(tableName);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return jdbcField;
    }

    private String convertStrToSnakeCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar) && i != 0) {
                result.append("_");
            }
            result.append(Character.toLowerCase(currentChar));
        }
        return result.toString();
    }

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
        if (CollectionUtil.isNotEmpty(conflictColumns)) {
            String conflictColumn = String.join(",", conflictColumns);
            return INSERT_ON_CONFLICT_TEMPLATE.replace("#tableName#", tableName).replace("#columns#", column).replace("#values#", values).replace("#conflictColumn#", conflictColumn);
        }
        return INSERT_TEMPLATE.replace("#tableName#", tableName).replace("#columns#", column).replace("#values#", values);
    }

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
        if (CollectionUtil.isNotEmpty(conflictColumns)) {
            String conflictColumn = String.join(",", conflictColumns);
            return INSERT_ON_CONFLICT_TEMPLATE.replace("#tableName#", tableName).replace("#columns#", column).replace("#values#", values).replace("#conflictColumn#", conflictColumn);
        }
        return INSERT_TEMPLATE.replace("#tableName#", tableName).replace("#columns#", column).replace("#values#", values);
    }

    protected List<String> getInsertValues(Object resource, Map<String, JdbcFieldProperty> columnMap, Set<String> columns) {
        List<String> valueList = new ArrayList<>();
        columns.forEach(column -> {
            JdbcFieldProperty property = columnMap.get(column);
            String value = getValue(resource, property, true);
            valueList.add(value);
        });
        return valueList;
    }

    private String getValue(Object resource, JdbcFieldProperty property, boolean useDefault) {
        if (resource == null) {
            return null;
        }
        Class<?> clazz = resource.getClass();
        String field = property.getField();
        JdbcFieldType type = property.getType();
        Object value;
        try {
            Field declaredField = clazz.getDeclaredField(field);
            declaredField.setAccessible(true);
            value = declaredField.get(resource);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e);
        }
        if (value == null) {
            if (useDefault) {
                value = property.getDefaultValue();
            }
            if (value == null) {
                return NULL;
            }
        }
        switch (type) {
            case STRING -> {
                return "'" + String.valueOf(value).replace("'", "''") + "'";
            }
            case BOOL -> {
                if (value instanceof Boolean bool && bool) {
                    return "true";
                }
                return "false";
            }
            case DATE -> {
                if (value instanceof Date v) {
                    return "'" + DateUtil.format(v, property.getDateFormat()) + "'";
                }
            }
            case TIMESTAMP -> {
                if (value instanceof Timestamp v) {
                    return "'" + DateUtil.format(v, property.getDateFormat()) + "'";
                }
            }

            case LOCAL_DATE -> {
                if (value instanceof LocalDate v) {
                    return "'" + DateUtil.format(v, property.getDateFormat()) + "'";
                }
            }
            case LOCAL_DATE_TIME -> {
                if (value instanceof LocalDateTime v) {
                    return "'" + DateUtil.format(v, property.getDateFormat()) + "'";
                }
            }
            case NUMBER -> {
                return "" + value;
            }
            default -> {
                return "'" + String.valueOf(value).replace("'", "''") + "'";
            }
        }
        return NULL;
    }


    protected String formatValue(Object value) {
        if (value == null) {
            return NULL;
        }
        if (value instanceof String str) {
            return "'" + str.replace("'", "''") + "'";
        } else if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Float) {
            return "" + value;
        } else if (value instanceof Date date) {
            return "'" + DateUtil.formatDateTime(date) + "'";
        } else if (value instanceof LocalDate localDate) {
            return "'" + DateUtil.formatDate(localDate) + "'";
        } else if (value instanceof LocalDateTime localDateTime) {
            return "'" + DateUtil.formatDateTime(localDateTime) + "'";
        }
        System.out.println("其他类型Value: " + value.getClass());
        return NULL;
    }


    public String update(List<UpdateCondition> resources) {
        if (CollectionUtil.isEmpty(resources)) {
            return null;
        }
        return resources.stream().map(this::update).collect(Collectors.joining());
    }

    public <T> String update(T update, T condition) {
        UpdateCondition<T> updateCondition = new UpdateCondition<>(update, condition);
        return update(updateCondition);
    }

    public <T> String update(UpdateCondition<T> resource) {
        if (resource == null) {
            return null;
        }
        T update = resource.getUpdate();
        T condition = resource.getCondition();
        if (update == null || condition == null) {
            throw new BaseException("Failed to build updateSql , because update or condition is null!").record();
        }
        JdbcField jdbcField = getJdbcClass(update);
        String tableName = jdbcField.getTableName();
        Map<String, JdbcFieldProperty> columnMap = jdbcField.getFields();
        Set<String> columns = columnMap.keySet();
        List<String> updateList = new ArrayList<>();
        List<String> conditionList = new ArrayList<>();
        columns.forEach(column -> {
            JdbcFieldProperty property = columnMap.get(column);
            String values = getValue(update, property, false);
            String conditions = getValue(condition, property, false);
            if (values != null && !values.equals(NULL)) {
                updateList.add("\"" + column + "\" = " + values);
            }
            if (conditions != null && !conditions.equals(NULL)) {
                conditionList.add("\"" + column + "\" = " + conditions);
            }
        });
        if (CollectionUtil.isEmpty(updateList)) {
            throw new BaseException("Update " + tableName + " must has set value!!").record();
        }
        if (CollectionUtil.isEmpty(conditionList)) {
            throw new BaseException("Update " + tableName + " must has condition!!").record();
        }
        String updateValue = String.join(", ", updateList);
        String conditionValue = String.join(" and ", conditionList);
        return UPDATE_TEMPLATE.replace("#tableName#", tableName).replace("#updates#", updateValue).replace("#conditions#", conditionValue);
    }


    /**
     * 返回 {@code clazz} 本身以及全部超类（直到 {@link Object}) 的字段列表。
     * <p>
     * 1. 结果顺序为“子类在前、父类在后”，这通常是我们想要的覆盖顺序。<br>
     * 2. 同名字段（子类隐藏父类字段）只会出现一次——子类的会保留，父类的会被过滤掉。<br>
     * 3. 合法字段均已调用 {@link Field#setAccessible(boolean)}，后续可以直接读取/写入。<br>
     *
     * @param clazz 需要检查的类，若为 {@code null} 抛 {@code IllegalArgumentException}
     * @return 不可变的 {@link List<Field>}，永远不为 {@code null}
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        // 用来保存发现的字段，保持 “子类 → 父类” 的顺序
        List<Field> fields = new ArrayList<>();

        // 为了去重（子类隐藏父类同名字段）准备一个名字集合
        java.util.Set<String> seenNames = new java.util.HashSet<>();

        // 依次遍历类层级
        for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
            Field[] declared = current.getDeclaredFields(); // 只返回当前类声明的字段
            for (Field f : declared) {
                // 跳过编译器生成的 synthetic 字段（如 lambda$…$0、this$0 等）
                if (f.isSynthetic()) {
                    continue;
                }

                // 子类同名字段会先加入，后面的父类同名字段会被过滤掉
                if (seenNames.add(f.getName())) {
                    // 打开私有/受保护字段的访问权限（JDK 9+ 在强封装模块里可能需要
                    //   --add-opens <module>/<pkg>=ALL-UNNAMED
                    //   或者使用 MethodHandles 替代）
                    f.setAccessible(true);
                    fields.add(f);
                }
            }
        }
        return Collections.unmodifiableList(fields);
    }
}
