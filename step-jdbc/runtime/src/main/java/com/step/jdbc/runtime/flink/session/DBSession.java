package com.step.jdbc.runtime.flink.session;

public abstract class DBSession {
    private Long databaseId;

    public abstract void close();

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }
}
