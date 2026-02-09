package com.step.jdbc.runtime.flink.dictionary.database.tdengine;


import java.util.ArrayList;
import java.util.List;

public class ColumnInfo {

    private String field;
    private String column;
    private DataType type;
    private boolean hasValue;
    private Object value;

    public ColumnInfo() {
        this.hasValue = false;
    }

    public ColumnInfo(int field, String column, DataType type) {
        this.field = "" + field;
        this.column = column;
        this.type = type;
    }

    public ColumnInfo(String column, DataType type) {
        this.column = column;
        this.type = type;
    }

    public static List<ColumnInfo> columnInfoInit(String pk) {
        List<ColumnInfo> columnInfos = new ArrayList<>();
        //加入时间栏位
//        ColumnInfo ts = new ColumnInfo();
//        ts.setColumn("ts");
//        ts.setHasValue(true);
//        ts.setType(DataType.Timestamp);
//        ts.setField("ts");
//        ts.setValue(System.currentTimeMillis());
//        columnInfos.add(ts);
        //加入主键
        ColumnInfo pkColumn = new ColumnInfo();
        pkColumn.setColumn(pk);
        pkColumn.setHasValue(true);
        pkColumn.setType(DataType.STRING);
        pkColumn.setField("-1");
        pkColumn.setValue(null);
        columnInfos.add(pkColumn);
        return columnInfos;
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

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public boolean isHasValue() {
        return hasValue;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
