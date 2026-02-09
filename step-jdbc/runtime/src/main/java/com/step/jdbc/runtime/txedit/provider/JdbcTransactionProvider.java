package com.step.jdbc.runtime.txedit.provider;

import com.step.api.runtime.core.JdbcTransactionAPI;
import com.step.jdbc.runtime.txedit.config.TransactionConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.inject.Singleton;


/**
 * @author : Sun
 * @date : 2023/1/5  10:40
 */
@Singleton
public class JdbcTransactionProvider implements JdbcTransactionAPI {

    @Override
    public DriverManagerDataSource createDataSource(String url, String driver, String userName, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Override
    public JdbcTemplate createJdbc(String url, String driver, String userName, String password) {
        return new JdbcTemplate(createDataSource(url, driver, userName, password));
    }

    @Override
    public JdbcTemplate createJdbc(DriverManagerDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Override
    public DataSourceTransactionManager getTransactionManager(String url, String driver, String userName, String password) {
        return new DataSourceTransactionManager(createDataSource(url, driver, userName, password));
    }

    @Override
    public DataSourceTransactionManager getTransactionManager(DriverManagerDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public TransactionStatus begin(DataSourceTransactionManager transactionManager, DefaultTransactionDefinition definition) {
        return transactionManager.getTransaction(definition);
    }

    @Override
    public TransactionStatus begin(DataSourceTransactionManager transactionManager) {
        return transactionManager.getTransaction(defaultTransactionDefinition());
    }

    @Override
    public void commit(DataSourceTransactionManager transactionManager, TransactionStatus transactionStatus) {
        transactionManager.commit(transactionStatus);
    }

    @Override
    public void rollback(DataSourceTransactionManager transactionManager, TransactionStatus transactionStatus) {
        transactionManager.rollback(transactionStatus);
    }

    private DefaultTransactionDefinition defaultTransactionDefinition() {
        //设置属性的默认属性
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        //设置事务的传播行为，此处是设置为开启一个新事务
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        //设置事务的隔离级别，此处是读已经提交
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        //设置超时时间
        definition.setTimeout(TransactionConfig.TIME_OUT);
        //获取事务状态对象
        return definition;
    }
}
