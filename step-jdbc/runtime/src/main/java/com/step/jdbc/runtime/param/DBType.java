package com.step.jdbc.runtime.param;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.txedit.exception.JdbcError;

import java.util.Optional;

/**
 * @author : Sun
 * @date : 2023/1/28  13:05
 */
public enum DBType {
    /**
     * 数据库驱动类
     */
    MYSQL("com.mysql.cj.jdbc.Driver", 3306, "jdbc:mysql://%s:%s/%s", "SELECT version()"),
    ORACLE("oracle.jdbc.driver.OracleDriver", 1521, "jdbc:oracle:thin:@%s:%s/%s", ""),
    POSTGRESQL("org.postgresql.Driver", 5432, "jdbc:postgresql://%s:%s/%s", "SELECT version()"),
    TDGENINE("com.taosdata.jdbc.rs.RestfulDriver", 6041, "jdbc:TAOS-RS://%s:%s/%s", "SHOW DATABASES"),
    //DB2("com.ibm.db2.jcc.DBDriver"),
    //DERBY("org.apache.derby.jdbc.ClientDriver"),
    //H2("org.h2.Driver"),
    //MARIADB("org.mariadb.jdbc.Driver"),
    //MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    ;

    DBType(String driver, int defaultPort, String urlTemplate, String testSQL) {
        this.driver = driver;
        this.defaultPort = defaultPort;
        this.urlTemplate = urlTemplate;
        this.testSQL = testSQL;
    }

    private final String driver;
    private final int defaultPort;
    private final String urlTemplate;
    private final String testSQL;

    public String getDriver() {
        return driver;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public String getTestSQL() {
        return testSQL;
    }

}
