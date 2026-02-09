package com.step.api.runtime.core;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author : Sun
 * @date : 2023/1/5  10:38
 */
public interface JdbcTransactionAPI {

    /**
     * 自定义jdbc开启事务
     */
    DriverManagerDataSource createDataSource(String url, String driver, String userName, String password);

    JdbcTemplate createJdbc(String url, String driver, String userName, String password);

    JdbcTemplate createJdbc(DriverManagerDataSource dataSource);

    DataSourceTransactionManager getTransactionManager(String url, String driver, String userName, String password);

    DataSourceTransactionManager getTransactionManager(DriverManagerDataSource dataSource);

    TransactionStatus begin(DataSourceTransactionManager transactionManager, DefaultTransactionDefinition definition);

    TransactionStatus begin(DataSourceTransactionManager transactionManager);

    void commit(DataSourceTransactionManager transactionManager, TransactionStatus transactionStatus);

    void rollback(DataSourceTransactionManager transactionManager, TransactionStatus transactionStatus);

}
