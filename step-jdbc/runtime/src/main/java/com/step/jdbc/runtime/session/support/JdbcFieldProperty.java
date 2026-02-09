package com.step.jdbc.runtime.session.support;


/**
 * @author : Sun
 * @date : 2024/4/24  9:23
 */

public class JdbcFieldProperty {
    JdbcFieldProperty() {
    }

    private boolean primaryKey;
    private PrimaryKeyType primaryKeyType;
    private String field;
    private String column;
    private Boolean insertIgnore;
    private JdbcFieldType type;
    private String length;
    private String defaultValue;
    private String dateFormat;
    private int sort;

    public PrimaryKeyType getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(PrimaryKeyType primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Boolean getInsertIgnore() {
        return insertIgnore;
    }

    public void setInsertIgnore(Boolean insertIgnore) {
        this.insertIgnore = insertIgnore;
    }

    public JdbcFieldType getType() {
        return type;
    }

    public void setType(JdbcFieldType type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
