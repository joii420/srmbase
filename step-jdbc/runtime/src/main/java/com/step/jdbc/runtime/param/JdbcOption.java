package com.step.jdbc.runtime.param;

import com.step.jdbc.runtime.session.converter.DefaultConverter;
import com.step.jdbc.runtime.session.converter.ResultConverter;

/**
 * @author : Sun
 * @date : 2024/1/28  15:36
 */
public class JdbcOption {

    private boolean transaction;
    private boolean log;
    private String pool;
    private ResultConverter converter;
    private boolean filterNull;
    private boolean test;

    public JdbcOption() {
        //默认开启事务和日志打印
        this.transaction = true;
        this.log = true;
        this.filterNull = false;
        this.converter = new DefaultConverter();
        this.pool = null;
        this.test = false;
    }

    public ResultConverter getConverter() {
        return converter;
    }

    public boolean getFilterNull() {
        return filterNull;
    }

    public void setFilterNull(boolean filterNull) {
        this.filterNull = filterNull;
    }

    public void setConverter(ResultConverter converter) {
        this.converter = converter;
    }

    public boolean getTransaction() {
        return transaction;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public boolean getTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean getLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

}
