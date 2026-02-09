package com.step.jdbc.runtime.flink.dictionary.database.tdengine;

public enum DataType {
    TIMESTAMP("TIMESTAMP"),
    DATE("TIMESTAMP"),
    STRING("NCHAR(255)"),
    DOUBLE("DOUBLE"),
    INTEGER("INT"),
    INT("INT"),
    LONG("BIGINT"),
    FLOAT("FLOAT"),
    BOOLEAN("BOOLEAN"),

    ;
    private final String sqlType;

    DataType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlType() {
        return sqlType;
    }
}