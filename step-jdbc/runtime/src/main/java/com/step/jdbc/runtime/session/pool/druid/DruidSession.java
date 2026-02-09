package com.step.jdbc.runtime.session.pool.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

public class DruidSession {
    public void openSession() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("your.database.driver.classname");
        dataSource.setUrl("jdbc:your_database_url");
        dataSource.setUsername("your_database_username");
        dataSource.setPassword("your_database_password");
        dataSource.setDbType(DbType.postgresql);
        dataSource.setInitialSize(3);
//        DruidPooledConnection connection = dataSource.getConnection();
    }


}
