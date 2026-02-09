package com.step.jdbc.runtime.flink.session;

import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.session.JdbcSession;

public class RelationDBSession extends DBSession {

    private boolean isOpen;

    private RelationDatabaseThreadPool relationDatabaseThreadPool;

    @Override
    public void close() {
        if (relationDatabaseThreadPool != null) {
            relationDatabaseThreadPool.shutdown();
        }
        isOpen = false;
    }

    public JdbcSession getJdbcSession() {
        return relationDatabaseThreadPool.getConnection();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public RelationDBSession(JdbcParam jdbcParam, int size) {
        relationDatabaseThreadPool = new RelationDatabaseThreadPool(jdbcParam, new JdbcOption(), size);
    }

    public RelationDBSession(JdbcParam jdbcParam, JdbcOption option, int size) {
        relationDatabaseThreadPool = new RelationDatabaseThreadPool(jdbcParam, option, size);
    }

    public void relase(JdbcSession jdbcSession) {
        this.relationDatabaseThreadPool.releaseConnection(jdbcSession);
    }
}
