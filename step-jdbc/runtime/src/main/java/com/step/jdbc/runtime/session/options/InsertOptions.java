package com.step.jdbc.runtime.session.options;

import java.util.Set;

/**
 * @author : Sun
 * @date : 2024/6/18  16:05
 */
public class InsertOptions {

    private boolean autoCreateTableIfNotExist = false;

    /**
     * 冲突不执行字段
     */
    private Set<String> conflictColumns;

    public boolean getAutoCreateTableIfNotExist() {
        return autoCreateTableIfNotExist;
    }

    public void setAutoCreateTableIfNotExist(boolean autoCreateTableIfNotExist) {
        this.autoCreateTableIfNotExist = autoCreateTableIfNotExist;
    }

    public Set<String> getConflictColumns() {
        return conflictColumns;
    }

    public void setConflictColumns(Set<String> conflictColumns) {
        this.conflictColumns = conflictColumns;
    }
}
