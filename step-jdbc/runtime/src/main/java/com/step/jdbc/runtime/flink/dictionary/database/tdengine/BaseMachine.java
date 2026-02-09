package com.step.jdbc.runtime.flink.dictionary.database.tdengine;

import java.sql.Timestamp;

public class BaseMachine {

    private Timestamp ts;

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public void insert() {

    }

//    public List<Object> query(List<Condition> conditions) {
//        Class<? extends BaseMachine> clazz = this.getClass();
//        return TDengineUtils.query(clazz, clazz.getSimpleName().toLowerCase(), conditions);
//    }

    @Override
    public String toString() {
        return "BaseMechine{" +
                "ts=" + ts +
                '}';
    }
}
