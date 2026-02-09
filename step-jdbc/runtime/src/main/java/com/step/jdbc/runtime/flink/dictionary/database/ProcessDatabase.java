package com.step.jdbc.runtime.flink.dictionary.database;


import com.step.jdbc.runtime.flink.dictionary.enums.DBType;

import java.io.Serializable;

public class ProcessDatabase implements Serializable {

    private Long id;

    private DBType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DBType getType() {
        return type;
    }

    public void setType(DBType type) {
        this.type = type;
    }
}
