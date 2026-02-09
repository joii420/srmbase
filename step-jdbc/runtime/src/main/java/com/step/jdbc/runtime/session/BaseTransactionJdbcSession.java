package com.step.jdbc.runtime.session;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.session.options.InsertOptions;
import com.step.jdbc.runtime.session.support.JdbcSqlBuilder;
import com.step.jdbc.runtime.session.support.UpdateCondition;
import com.step.jdbc.runtime.txedit.exception.JdbcError;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author : Sun
 * @date : 2024/4/24  12:55
 */
public abstract class BaseTransactionJdbcSession extends BaseJdbcSession {

    private boolean active;

    private final String sessionId;
    private final Connection connection;
    private final Statement statement;
    private final JdbcOption option;
    private final JdbcSqlBuilder sqlBuilder;
    private boolean unCommit;
    private long lastActiveTime;
    private final int timeout = 5 * 60 * 1000;
    protected final String mountPool;


    protected BaseTransactionJdbcSession(String sessionId, Connection connection, Statement statement, JdbcOption option, JdbcSqlBuilder sqlBuilder, String mountPool) {
        super(sqlBuilder, option);
        this.sessionId = sessionId;
        this.mountPool = mountPool;
        this.connection = connection;
        this.statement = statement;
        this.option = option;
        this.sqlBuilder = sqlBuilder;
        this.active = true;
        this.unCommit = false;
        this.lastActiveTime = System.currentTimeMillis();
    }

    protected boolean hasUnCommit() {
        return this.unCommit;
    }

    public String getSessionId() {
        return sessionId;
    }

    protected void checkActive() {
        refreshActive();
        if (!isActive()) {
            throw new BaseException(JdbcError.CON_007.getMessage()).record();
        }
    }

    public boolean isActive() {
        try {
            boolean valid = this.connection.isValid(10);
            if (!valid) {
                log.info("Connection is invalid: " + this.sessionId);
                active = false;
            }
        } catch (SQLException e) {
            throw new BaseException(e);
        }
        return active;
    }

    public void commit() {
        if (this.option.getTransaction() && this.unCommit) {
            try {
                this.unCommit = false;
                this.connection.commit();
            } catch (SQLException e) {
                log.error("Commit data error: {}", e.getMessage(), e);
                throw new BaseException(e);
            }
        }
        this.unCommit = false;
    }

    public void rollback() {
        if (this.option.getTransaction() && this.unCommit) {
            this.unCommit = false;
            try {
                this.connection.rollback();
            } catch (SQLException e) {
                log.error("Rollback data error: {}", e.getMessage(), e);
                throw new BaseException(e);
            }
        }
        this.unCommit = false;
    }

    protected void closeFreeSession(long now) {
        if (!isActive()) {
            this.close();
        }
        if (!this.unCommit) {
            //超时自动关闭
            if (this.lastActiveTime + timeout < now) {
                log.info("Session is expired: {} ,active at: {}", this.sessionId, this.lastActiveTime);
                this.close();
            }
        }
    }

    protected void refreshActive() {
        this.lastActiveTime = System.currentTimeMillis();
    }

    protected void updateActive(boolean active) {
        this.active = active;
    }

    public int updateUnCommit(String updateSql) {
        return updateUnCommit(updateSql, null);
    }

    public int updateUnCommit(String updateSql, Map<String, Object> sqlParams) {
        checkActive();
        try {
            this.unCommit = true;
            return update(this.sessionId, statement, option, updateSql, sqlParams);
        } catch (BaseException e) {
            log.error("Sql execute error: {}", e.getMessage(), e);
            this.rollback();
            throw e;
        } catch (Exception e) {
            log.error("Sql execute error: {}", e.getMessage(), e);
            this.rollback();
            throw new BaseException(e);

        }
    }


    public List<Map<String, Object>> queryUnCommit(String querySql) {
        return queryUnCommit(querySql, null);
    }

    public List<Map<String, Object>> queryUnCommit(String querySql, Map<String, Object> sqlParams) {
        checkActive();
        try {
            this.unCommit = true;
            return query(this.sessionId, statement, option, querySql, sqlParams);
        } catch (BaseException e) {
            log.error("Sql execute error: {}", e.getMessage(), e);
            this.rollback();
            throw e;
        } catch (Exception e) {
            log.error("Sql execute error: {}", e.getMessage(), e);
            this.rollback();
            throw new BaseException(e);
        }
    }


    public void executeUnCommit(String sql) {
        executeUnCommit(sql, null);
    }

    public void executeUnCommit(String sql, Map<String, Object> sqlParams) {
        executeUnCommit(() -> execute(this.sessionId, statement, option, sql, sqlParams));
    }

    public <T> T executeBatchUnCommit(Function<JdbcSessionProxy, T> handler) {
        checkActive();
        try {
            this.unCommit = true;
            return handler.apply(new JdbcSessionProxy(this.sessionId, statement, option, sqlBuilder));
        } catch (BaseException e) {
            log.error("Sql execute error: {}", e.getMessage(), e);
            this.rollback();
            throw e;
        } catch (Exception e) {
            log.error("Sql execute error: {}", e.getMessage(), e);
            this.rollback();
            throw new BaseException(e);
        }
    }

    public void executeBatchUnCommit(Consumer<JdbcSessionProxy> handler) {
        executeUnCommit(() -> handler.accept(new JdbcSessionProxy(this.sessionId, statement, option, sqlBuilder)));
    }

    public List<Map<String, Object>> insertUnCommit(String tableName, Map<String, Object> resource) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, tableName, resource));
    }

    public List<Map<String, Object>> insertUnCommit(String tableName, Map<String, Object> resource, InsertOptions insertOptions) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, tableName, resource, insertOptions));
    }

    public List<Map<String, Object>> insertUnCommit(String tableName, List<Map<String, Object>> resources) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, tableName, resources));
    }

    public List<Map<String, Object>> insertUnCommit(String tableName, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, tableName, resources, insertOptions));
    }

    public List<Map<String, Object>> insertUnCommit(String tableName, Set<String> columns, List<Map<String, Object>> resources) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, tableName, columns, resources));
    }

    public List<Map<String, Object>> insertUnCommit(String tableName, Set<String> columns, List<Map<String, Object>> resources, InsertOptions insertOptions) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, tableName, columns, resources, insertOptions));
    }

    public void createTableUnCommit(Object resources) {
        executeUnCommit(() -> createTable(this.sessionId, statement, option, resources));
    }

    public List<Map<String, Object>> insertSqlUnCommit(String insertSql) {
        return insertSqlUnCommit(insertSql, null);
    }

    public List<Map<String, Object>> insertSqlUnCommit(String insertSql, Map<String, Object> sqlParams) {
        checkActive();
        try {
            this.unCommit = true;
            return insert(this.sessionId, statement, insertSql, sqlParams);
        } catch (BaseException e) {
            this.rollback();
            throw e;
        } catch (Exception e) {
            this.rollback();
            throw new BaseException(e);
        }
    }

    public List<Map<String, Object>> insertUnCommit(Object resource) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, resource));
    }

    public List<Map<String, Object>> insertUnCommit(Object resource, InsertOptions insertOptions) {
        return executeUnCommit(() -> insert(this.sessionId, statement, option, resource, insertOptions));
    }


    public int updateUnCommit(List<UpdateCondition> resources) {
        return executeUnCommit(() -> update(this.sessionId, statement, option, resources));
    }

    public int updateUnCommit(UpdateCondition... resources) {
        return updateUnCommit(Arrays.stream(resources).toList());
    }

    public <T> int updateUnCommit(UpdateCondition<T> resources) {
        return executeUnCommit(() -> update(this.sessionId, statement, option, resources));
    }

    private void executeUnCommit(Runnable runnable) {
        checkActive();
        try {
            this.unCommit = true;
            runnable.run();
        } catch (BaseException e) {
            this.rollback();
            throw e;
        } catch (Exception e) {
            this.rollback();
            throw new BaseException(e);
        }
    }

    private <T> T executeUnCommit(Supplier<T> supplier) {
        checkActive();
        try {
            this.unCommit = true;
            return supplier.get();
        } catch (BaseException e) {
            this.rollback();
            throw e;
        } catch (Exception e) {
            this.rollback();
            throw new BaseException(e);
        }
    }


    public void close() {
        log.info("Close session: {}", this.sessionId);
        updateActive(false);
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            log.error("Failed to close session: {}", e.getMessage(), e);
        } finally {
            JdbcSessionManager.removeSession(sessionId);
        }
    }
}
