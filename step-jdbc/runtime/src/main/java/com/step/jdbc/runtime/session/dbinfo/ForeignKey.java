package com.step.jdbc.runtime.session.dbinfo;

public class ForeignKey {

    private String foreignTableName;
    private String foreignColumnName;
    private String fkTableName;
    private String fkColumnName;

    public String getForeignTableName() {
        return foreignTableName;
    }

    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    public String getFkColumnName() {
        return fkColumnName;
    }

    public void setFkColumnName(String fkColumnName) {
        this.fkColumnName = fkColumnName;
    }
}
