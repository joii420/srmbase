package com.step.jdbc.runtime.txedit.model.enums;

/**
 * @author : Sun
 * @date : 2023/1/5  13:05
 */
public enum EditType {

    MAPPER(1),
    JDBC(2),
    ;

    EditType(int editType) {
        this.editType = editType;
    }

    private int editType;

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }
}
