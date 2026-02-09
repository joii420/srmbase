package com.step.jdbc.runtime.flink.process;


import com.step.jdbc.runtime.flink.dictionary.database.ProcessDatabase;
import com.step.jdbc.runtime.flink.dictionary.datasource.SelectDatasource;

import java.util.Map;

public class CollectProcessDatabase {

    private SelectDatasource processDatasource;

    private ProcessDatabase processDatabase;

    private Map<String,String> datas;

    public SelectDatasource getProcessDatasource() {
        return processDatasource;
    }

    public void setProcessDatasource(SelectDatasource processDatasource) {
        this.processDatasource = processDatasource;
    }

    public ProcessDatabase getProcessDatabase() {
        return processDatabase;
    }

    public void setProcessDatabase(ProcessDatabase processDatabase) {
        this.processDatabase = processDatabase;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }
}
