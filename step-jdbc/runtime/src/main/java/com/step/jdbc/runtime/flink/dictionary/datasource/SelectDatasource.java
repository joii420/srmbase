package com.step.jdbc.runtime.flink.dictionary.datasource;

import com.step.jdbc.runtime.flink.dictionary.database.ProcessDatabase;
import com.step.jdbc.runtime.flink.dictionary.enums.DSType;

import java.io.Serializable;

public class SelectDatasource extends FlinkBaseDatasource implements Serializable {

    private String command;

    private ProcessDatabase processDatabase;  //对应数据库

    public SelectDatasource(String command, String name, boolean isMain, boolean isInit, String[] resultFields) {
        this.setDsType(DSType.SELECT);
        this.command = command;
        this.setName(name);
        this.setMain(isMain);
        this.setInit(isInit);
        this.setResultFields(resultFields);
    }

    @Deprecated
    public SelectDatasource(String command, String name, String alias, Long id, Long databaseId, String databaseType, boolean isMain, String[] resultFields) {
        this.command = command;
        this.setName(name);
        this.setMain(isMain);
        this.setResultFields(resultFields);
    }

    public SelectDatasource() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ProcessDatabase getProcessDatabase() {
        return processDatabase;
    }

    public void setProcessDatabase(ProcessDatabase processDatabase) {
        this.processDatabase = processDatabase;
    }
}
