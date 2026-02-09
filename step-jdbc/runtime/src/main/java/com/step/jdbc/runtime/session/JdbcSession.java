package com.step.jdbc.runtime.session;

import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.session.dbinfo.Column;
import com.step.jdbc.runtime.session.dbinfo.ForeignKey;
import com.step.jdbc.runtime.session.dbinfo.Procedure;
import com.step.jdbc.runtime.session.dbinfo.Table;
import com.step.jdbc.runtime.session.options.InsertOptions;
import com.step.jdbc.runtime.session.support.JdbcSqlBuilder;
import com.step.jdbc.runtime.session.support.UpdateCondition;
import com.step.api.runtime.exception.base.BaseException;
import com.step.logger.LOGGER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcSession extends BaseCloseConnectJdbcSession implements JdbcSessionInterface, Serializable {
    public static final Logger log = LoggerFactory.getLogger(JdbcSession.class);
    private final String sessionId;
    private final Connection connection;
    private final Statement statement;
    private final JdbcParam jdbcParam;
    private final LocalDateTime createTime;
    private final String poolName;
    private final JdbcOption option;


    JdbcSession(String sessionId, Connection connection, Statement statement, JdbcParam jdbcParam, JdbcOption option, String poolName) {
        super(sessionId, connection, statement, option, JdbcSqlBuilder.from(jdbcParam.getDbType()), poolName);
        this.sessionId = sessionId;
        this.connection = connection;
        this.createTime = LocalDateTime.now();
        this.poolName = poolName;
        this.statement = statement;
        this.option = option;
        this.jdbcParam = jdbcParam;
        JdbcSessionManager.recordSession(this);
        if (this.option.getLog()) {
            LOGGER.info(log, "创建JDBC会话: %s [ %s ] %n", this.sessionId, this.jdbcParam.getUrl());
        }
    }

    @Override
    public void execute(String sql, Map<String, Object> sqlParams) {
        try {
            executeUnClose(sql, sqlParams);
        } finally {
            this.close();
        }
    }


    @Override
    public int update(String updateSql, Map<String, Object> sqlParams) {
        try {
            return updateUnClose(updateSql, sqlParams);
        } finally {
            this.close();
        }
    }


    @Override
    public <T> T queryOne(String querySql, Map<String, Object> sqlParams, Class<T> clazz) {
        try {
            return queryOneUnClose(querySql, sqlParams, clazz);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> query(String querySql, Map<String, Object> sqlParams) {
        try {
            return queryUnClose(querySql, sqlParams);
        } finally {
            this.close();
        }
    }

    @Override
    public <T> List<T> query(String querySql, Map<String, Object> sqlParams, Class<T> clazz) {
        try {
            return queryUnClose(querySql, sqlParams, clazz);
        } finally {
            this.close();
        }
    }

    @Override
    public int queryCount(String querySql, Map<String, Object> sqlParams) {
        checkActive();
        try {
            return queryCount(this.sessionId, statement, option, querySql, sqlParams);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insertSql(String insertSql, Map<String, Object> sqlParams) {
        try {
            return insertUnClose(insertSql, sqlParams);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Map<String, Object> resource) {
        try {
            return insertUnClose(tableName, resource);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Map<String, Object> resource, InsertOptions insertOptions) {
        try {
            return insertUnClose(tableName, resource, insertOptions);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources) {
        try {
            return insertUnClose(tableName, resources);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        try {
            return insertUnClose(tableName, resources, insertOptions);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources) {
        try {
            return insertUnClose(tableName, columns, resources);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        try {
            return insertUnClose(tableName, columns, resources, insertOptions);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(Object resource) {
        try {
            if (resource instanceof String insertSql) {
                return this.insertSqlUnClose(insertSql);
            }
            return insertUnClose(resource);
        } finally {
            this.close();
        }
    }

    @Override
    public List<Map<String, Object>> insert(Object resource, InsertOptions insertOptions) {
        try {
            return insertUnClose(resource, insertOptions);
        } finally {
            this.close();
        }
    }

    @Override
    public void createTable(Object resource) {
        try {
            createTableUnClose(resource);
        } finally {
            this.close();
        }
    }


    @Override
    public <T> int update(UpdateCondition<T> resource) {
        try {
            return updateUnClose(resource);
        } finally {
            this.close();
        }
    }

    @Override
    public int update(List<UpdateCondition> resources) {
        try {
            return updateUnClose(resources);
        } finally {
            this.close();
        }
    }

    public <T> T executeBatch(Function<JdbcSessionProxy, T> handler) {
        try {
            return executeBatchUnClose(handler);
        } finally {
            this.close();
        }
    }

    public void executeBatch(Consumer<JdbcSessionProxy> handler) {
        try {
            executeBatchUnClose(handler);
        } finally {
            this.close();
        }
    }

    /**
     * 获取库中的表结构、字段、方法、存储过程、表关系等内容
     */
    public List<Table> getERStructure() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            List<Table> tableList = new ArrayList<>();
            //如果需要整个数据库的内容 则不需要这个字段
            String catalog = connection.getCatalog();
            // 获取表信息
            ResultSet tables = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                Table table = new Table();
                table.setName(tableName);
//                System.out.println("tableName:"+tableName);
                // 获取表的字段信息
                List<Column> columnList = new ArrayList<>();
                ResultSet columns = metaData.getColumns(null, null, tableName, null);
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    boolean isNullable = (columns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls);
                    String remarks = columns.getString("REMARKS");

                    Column column = new Column();
                    column.setName(columnName);
                    column.setType(dataType);
                    column.setLength(columnSize);
                    column.setNullable(isNullable);
                    column.setRemarks(remarks);
                    columnList.add(column);
                }
                table.setColumns(columnList);
                // 获取表之间的外键关系
                ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, tableName);
                List<ForeignKey> foreignKeyList = new ArrayList<>();
                while (foreignKeys.next()) {
                    String foreignTableName = foreignKeys.getString("PKTABLE_NAME");
                    String foreignColumnName = foreignKeys.getString("PKCOLUMN_NAME");
                    String fkTableName = foreignKeys.getString("FKTABLE_NAME");
                    String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                    ForeignKey foreign = new ForeignKey();
                    foreign.setForeignTableName(foreignTableName);
                    foreign.setForeignColumnName(foreignColumnName);
                    foreign.setFkTableName(fkTableName);
                    foreign.setFkColumnName(fkColumnName);
                    foreignKeyList.add(foreign);
                }
                table.setForeignKeys(foreignKeyList);

                // 获取表的函数信息
                List<com.step.jdbc.runtime.session.dbinfo.Function> functionList = new ArrayList<>();
                ResultSet functions = metaData.getFunctions(null, null, tableName);
                while (functions.next()) {
                    String functionName = functions.getString("FUNCTION_NAME");
                    String functionType = functions.getString("FUNCTION_TYPE");
                    com.step.jdbc.runtime.session.dbinfo.Function function = new com.step.jdbc.runtime.session.dbinfo.Function();
                    function.setName(functionName);
                    function.setType(functionType);
                    functionList.add(function);
                }
                table.setFunctions(functionList);

                // 获取表的存储过程信息
                List<Procedure> procedureList = new ArrayList<>();
                ResultSet procedures = metaData.getProcedures(null, null, tableName);
                while (procedures.next()) {
                    String procedureName = procedures.getString("PROCEDURE_NAME");
                    Procedure procedureObj = new Procedure();
                    procedureObj.setName(procedureName);
                    procedureList.add(procedureObj);
                }
                table.setProcedures(procedureList);
                tableList.add(table);
            }
            return tableList;
        } catch (Exception e) {
            throw new BaseException(e);
        } finally {
            this.close();
        }
    }


    public String getPoolName() {
        return poolName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

}